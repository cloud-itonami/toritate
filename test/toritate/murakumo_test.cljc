(ns toritate.murakumo-test
  (:require [clojure.test :refer [deftest is]]
            [toritate.murakumo :as toritate]))

(def full-attestations
  (into {}
        (map (fn [gate] [gate (str "attested-" (name gate))]))
        (distinct (mapcat :required-gates (vals toritate/cell-specs)))))

(deftest maps-all-legacy-toritate-cells
  (is (= #{"toritate_commons_asset_value"
           "toritate_imputed_income_compute"}
         (set (map :legacy-cell (vals toritate/cell-specs))))))

(deftest r0-gates-block-effects
  (let [plan (toritate/cell-plan :imputed-income-compute
                                 {:period "2026-Q2"
                                  :valuation-table-cid "bafkreitable"})]
    (is (= :blocked (:status plan)))
    (is (= [:council-lv6-ratification
            :toritate-baseline-review
            :valuation-method-table-attested-baseline
            :open-citable-valuation-source-baseline
            :encrypted-per-adherent-input-baseline
            :aggregate-only-no-leaderboard-baseline
            :cash-stipend-zero-baseline
            :murakumo-only-inference-baseline
            :kotoba-only-substrate-baseline
            :transparent-force-audit-baseline
            :flow-valuation-table-attested-baseline
            :in-kind-services-only-baseline
            :basic-high-income-feed-baseline
            :no-cash-benefit-baseline]
           (:missing-gates plan)))
    (is (empty? (:effects plan)))))

(deftest imputed-income-remains-cash-zero-and-aggregate-only
  (let [plan (toritate/cell-plan :imputed-income-compute
                                 {:attestations full-attestations
                                  :period "2026-Q2"
                                  :valuation-table-cid "bafkreiflowtable"
                                  :aggregate-id "flow-2026-q2"
                                  :median-usd-micros 1000000
                                  :total-usd-micros 10000000})
        effect (first (:effects plan))]
    (is (= :ready (:status plan)))
    (is (= "com.etzhayyim.toritate.imputedIncomeAggregate" (:collection effect)))
    (is (= 0 (get-in effect [:record :cashStipendUsdMicros])))
    (is (= true (get-in effect [:record :aggregateOnly])))
    (is (= false (get-in effect [:record :perAdherentPublicDetail])))))

(deftest commons-asset-value-is-access-not-title
  (let [plan (toritate/cell-plan :commons-asset-value
                                 {:attestations full-attestations
                                  :period "2026-Q2"
                                  :valuation-table-cid "bafkreistocktable"
                                  :aggregate-id "stock-2026-q2"})
        effect (first (:effects plan))]
    (is (= :ready (:status plan)))
    (is (= "com.etzhayyim.toritate.commonsAssetAccessAggregate" (:collection effect)))
    (is (= true (get-in effect [:record :accessNotTitle])))
    (is (= true (get-in effect [:record :nonAlienableAccess])))
    (is (= false (get-in effect [:record :transferableTitle])))
    (is (= false (get-in effect [:record :collateralizable])))
    (is (= false (get-in effect [:record :doubleCountWithFlow])))))

(deftest commons-asset-value-requires-no-double-count-gate
  (let [attestations (dissoc full-attestations :no-double-count-with-flow-baseline)
        plan (toritate/cell-plan :commons-asset-value
                                 {:attestations attestations
                                  :period "2026-Q2"})]
    (is (= :blocked (:status plan)))
    (is (= [:no-double-count-with-flow-baseline] (:missing-gates plan)))))

(deftest all-cell-plans-ready-when-attested
  (let [plans (toritate/all-cell-plans {:attestations full-attestations
                                        :period "2026-Q2"
                                        :valuation-table-cid "bafkreitable"
                                        :source-cids ["bafkreisource"]
                                        :aggregate-id "toritate-2026-q2"
                                        :median-usd-micros 1000000
                                        :total-usd-micros 10000000
                                        :computed-at "2026-06-29T00:00:00Z"})]
    (is (= (set (keys toritate/cell-specs)) (set (keys plans))))
    (is (every? #(= :ready (:status %)) (vals plans)))
    (is (= 2 (count (mapcat :effects (vals plans)))))))
