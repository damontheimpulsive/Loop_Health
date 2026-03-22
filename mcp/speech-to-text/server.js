import { readFile } from "node:fs/promises";
import { basename } from "node:path";

import { McpServer } from "@modelcontextprotocol/sdk/server/mcp.js";
import { StdioServerTransport } from "@modelcontextprotocol/sdk/server/stdio.js";
import { z } from "zod";

const OPENAI_API_KEY = process.env.OPENAI_API_KEY;
const OPENAI_TRANSCRIBE_URL = "https://api.openai.com/v1/audio/transcriptions";
const MODEL = "whisper-1";

function guessMimeType(filePath) {
  const lower = filePath.toLowerCase();
  if (lower.endsWith(".mp3")) return "audio/mpeg";
  if (lower.endsWith(".wav")) return "audio/wav";
  if (lower.endsWith(".m4a")) return "audio/mp4";
  if (lower.endsWith(".mp4")) return "audio/mp4";
  if (lower.endsWith(".webm")) return "audio/webm";
  return "application/octet-stream";
}

async function transcribeWithOpenAI({ audioPath }) {
  if (!OPENAI_API_KEY || !OPENAI_API_KEY.trim()) {
    throw new Error("Missing OPENAI_API_KEY environment variable");
  }

  const bytes = await readFile(audioPath);
  const fileName = basename(audioPath);
  const mimeType = guessMimeType(audioPath);

  const form = new FormData();
  form.set("model", MODEL);
  form.set("file", new Blob([bytes], { type: mimeType }), fileName);

  const res = await fetch(OPENAI_TRANSCRIBE_URL, {
    method: "POST",
    headers: {
      Authorization: `Bearer ${OPENAI_API_KEY}`
    },
    body: form
  });

  const text = await res.text();
  if (!res.ok) {
    throw new Error(`OpenAI STT error (${res.status}): ${text}`);
  }

  let json;
  try {
    json = JSON.parse(text);
  } catch {
    throw new Error(`Unexpected OpenAI STT response: ${text}`);
  }

  if (typeof json.text !== "string") {
    throw new Error(`Unexpected OpenAI STT response: ${text}`);
  }

  return json.text;
}

const server = new McpServer({
  name: "loop-health-speech-to-text",
  version: "1.0.0"
});

server.registerTool(
  "speech_to_text",
  {
    description:
      "Transcribe an audio file (local path) into text using OpenAI Whisper.",
    inputSchema: {
      audio_path: z
        .string()
        .min(1)
        .describe("Local path to audio file (e.g. test_audio.mp3)")
    }
  },
  async ({ audio_path }) => {
    const transcript = await transcribeWithOpenAI({ audioPath: audio_path });

    // 👉 Create output file name
    const outputFile = `${audio_path}.transcript.txt`;

    // 👉 Write to file
    await writeFile(outputFile, transcript, "utf-8");

    console.log(`Transcript saved to ${outputFile}`);
    console.log(transcript);
    return {
      content: [
        {
          type: "text",
          text: `Transcript saved to ${outputFile}\n\n${transcript}`
        }
      ]
    };
  }
);


if (process.argv[2]) {
  const audioPath = process.argv[2];

  transcribeWithOpenAI({ audioPath })
    .then((text) => {
      console.log("\n=== TRANSCRIPT ===\n");
      console.log(text);
    })
    .catch((err) => {
      console.error("Error:", err.message);
    });
}

const transport = new StdioServerTransport();
await server.connect(transport);

