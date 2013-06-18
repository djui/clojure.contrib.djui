(ns ^{:author "Uwe Dauernheim <uwe@dauernheim.net>"
      :doc "String helper functions."}
  clojure.contrib-djui.str)


(defn safe-parse
  "Parse a clojure term without evaluating anything."
  {:added "1.4"}
  [str]
  (binding [*read-eval* false] (read-string str)))
