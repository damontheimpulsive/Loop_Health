## Loop Health MCP: Speech to Text

This is an MCP server exposing a single tool: `speech_to_text`.

### Setup

```bash
cd "mcp/speech-to-text"
npm install
export OPENAI_API_KEY="..."
npm start
```

### Tool

- **name**: `speech_to_text`
- **input**:
  - `audio_path` (string): local path to an audio file (e.g. `test_audio.mp3`)
- **output**: transcript text

