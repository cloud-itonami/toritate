(require '[clojure.test :as t])
(def test-namespaces
  '[toritate.methods.test-charter-gates
    toritate.methods.test-imputed-income
    toritate.methods.test-securities-donation])
(doseq [ns-sym test-namespaces] (require ns-sym))
(let [result (apply t/run-tests test-namespaces)]
  (when-not (zero? (+ (:fail result) (:error result))) (System/exit 1)))
