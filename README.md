# toritate - Imputed Income Accounting Actor

**DID**: `did:web:toritate.etzhayyim.com`
**Namespace**: `com.etzhayyim.toritate.*`
**Status**: migration boundary for imputed-income accounting cells

## Migration Boundary

`src/toritate/murakumo.cljc` is the Murakumo-facing cljc actor boundary for the
legacy toritate kotoba-kotodama cells:

- `toritate_imputed_income_compute` -> `imputedIncomeAggregate`
- `toritate_commons_asset_value` -> `commonsAssetAccessAggregate`

The actor computes accounting records for aggregate liberation metrics only. It
does not emit cash benefits, per-adherent leaderboards, transferable title, or
collateralizable asset claims. Valuation tables must be open, citable, and
Council-attested before any plan emits records.
