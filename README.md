# toritate — accounting and audit actor

`toritate` (執帳) is the standalone Tier-B actor for transparent, non-profit accounting and audit projections over on-chain financial records.

## Layout

- `src/toritate/` — imputed-income, securities-donation, and social methods
- `test/toritate/` — deterministic charter and calculation tests
- `contracts/`, `data/`, and root metadata — canonical EDN
- `wire/contracts/`, `wire/valuation/`, and identity JSON — external projections
- `docs/` — ADR, valuation method, contracts, and maturity documentation

Run `kbb -M:test`.

The actor keeps the on-chain ledger primary, excludes payroll and commercial accounting software, publishes no donor PII, and does not render tax or accounting opinions.
