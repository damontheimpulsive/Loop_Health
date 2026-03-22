# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run Commands

```bash
# Build
./gradlew build

# Build without tests (for Docker deployment)
./gradlew build -x check

# Run tests
./gradlew test

# Run a single test class
./gradlew test --tests "com.demo.app.service.PrescriptionParserTest"

# Run the app with a transcript string
./gradlew run --args="Patient John Doe, prescribed Paracetamol 500mg twice daily for 5 days"

# Run the app with a transcript file
./gradlew run --args="audio.transcript.txt"
```

The build produces a fat JAR at `loop_health-all.jar` via the Shadow plugin.

### MCP Speech-to-Text Server

```bash
cd mcp/speech-to-text
npm install
node server.js          # Start MCP server (requires OPENAI_API_KEY)
node transcribe-file.js <audio-file>  # Standalone transcription utility
```

## Architecture

Loop Health extracts structured prescription data from medical transcripts (text or voice). Two parsing strategies are available:

**1. Regex-based (`PrescriptionParser`)** — Offline, fast pattern matching. Extracts patient name, doctor name, medications (name, dosage, frequency, duration), and handling instructions from transcript text.

**2. AI-based (`AIBasedPrescriptionParser`)** — Calls OpenAI GPT-3.5-turbo. Better for complex/ambiguous transcripts. Requires `OPENAI_API_KEY` environment variable.

**Entry flow:**
```
LoopHealthApp.main()
  → reads args (file path or transcript string)
  → PrescriptionParser.parse(transcript)
  → outputs pretty-printed JSON
```

**Output data model:**
```
PrescriptionData {
  prescriptionDate, patientName, doctorName,
  medications: [{ name, dosage, frequency, duration }],
  instructions
}
```

**MCP integration** (`mcp/speech-to-text/`): An MCP server exposing a `speech_to_text` tool that transcribes audio files (MP3, WAV, M4A, etc.) via OpenAI Whisper API, enabling voice → transcript → prescription JSON pipelines.

## Package Structure

Source lives under `com.demo.app` (migrated from `com.gopay.app`). Main classes:
- `LoopHealthApp` — entry point
- `service/PrescriptionParser` — regex parser
- `service/AIBasedPrescriptionParser` — OpenAI-based parser

## Code Review Standards (from `.cursor/` rules)

- **Prefer immutable objects**; use `Optional` carefully; avoid excessive object creation
- **Minimal targeted changes** — understand before modifying; no rewrites without clear improvement
- **Production safety** — check for null pointer risks, resource leaks, improper exception handling
- **Function length** — flag functions >50 lines; reduce nested conditionals
- **Edge cases** — always handle empty input, null values, boundary conditions
- **Logging** — important flows must have logs; errors must include context
