# toritate repository rules

- EDN is canonical for metadata, data, contract catalogs, and ADRs.
- External JSON, JSON-LD, and BPMN belong under `wire/` only.
- Production code belongs under `src/toritate`; tests belong under `test/toritate`.
- Do not restore monorepo-relative paths, Go/TinyGo ports, shell runners, generated WASM, or JSON-LD metadata.
- Preserve on-chain-primary, no-fiat-primary, no-payroll, aggregate-only, no-tax-advice, and Council-attestation gates.
- Run `bb test`, parse every EDN file, and audit artifacts before publishing.
