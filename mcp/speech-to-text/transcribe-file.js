import { readFile, mkdir, writeFile } from "node:fs/promises";
import { basename, dirname, resolve } from "node:path";

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

async function transcribeWithOpenAI(audioPath) {
  if (!OPENAI_API_KEY || !OPENAI_API_KEY.trim()) {
    throw new Error("Missing OPENAI_API_KEY environment variable");
  }

  const absoluteAudioPath = resolve(audioPath);
  const bytes = await readFile(absoluteAudioPath);
  const fileName = basename(absoluteAudioPath);
  const mimeType = guessMimeType(absoluteAudioPath);

  const form = new FormData();
  form.set("model", MODEL);
  form.set("file", new Blob([bytes], { type: mimeType }), fileName);

  const res = await fetch(OPENAI_TRANSCRIBE_URL, {
    method: "POST",
    headers: {
      Authorization: `Bearer ${OPENAI_API_KEY}`,
    },
    body: form,
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

async function main() {
  const [audioPathArg, outputPathArg] = process.argv.slice(2);

  if (!audioPathArg || !outputPathArg) {
    console.error(
      "Usage: node transcribe-file.js <audio_path> <output_text_path>"
    );
    console.error(
      'Example: node transcribe-file.js "../../test_audio.mp3" "../../output/test_audio.txt"'
    );
    process.exit(1);
  }

  const outputPath = resolve(outputPathArg);
  const outputDir = dirname(outputPath);

  try {
    await mkdir(outputDir, { recursive: true });
    const transcript = await transcribeWithOpenAI(audioPathArg);
    await writeFile(outputPath, transcript, "utf8");
    console.log(`Wrote transcript to: ${outputPath}`);
  } catch (err) {
    console.error(String(err?.stack || err));
    process.exit(1);
  }
}

// Run only when executed directly
if (process.argv[1] && basename(process.argv[1]) === "transcribe-file.js") {
  main();
}

