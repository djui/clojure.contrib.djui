(ns ^{:author "Uwe Dauernheim <uwe@dauernheim.net>"
      :doc "Core helper functions."}
  clojure.contrib-djui.core)


(defn unit
  "Returns the value in a collection if it only contains one item."
  {:added "1.0"}
  [coll]
  (when-let [coll (seq coll)]
    (when-not (next coll)
      (first coll))))

(defn any?
  "Returns a boolean if any value in coll satisfies pred."
  {:added "1.0"}
  [pred coll]
  (boolean (some pred coll)))

(defn any-true?
  "Returns a boolean if any value in coll is logically true."
  {:added "1.0"}
  [coll]
  (boolean (some identity coll)))

(defn |>
  "Piping/Thrushing, similar to F#'s or OCaml |>, or Haskells $.
  Limitations: Args are not evaluated lazily and functions can only take one
  argument (as last argument).
  Deprecated with Clojure 1.5, use clojure.core/-> or clojure.core/->> instead."
  {:added "1.0"
   :deprecated "1.1"}
  [& args]
  (reduce #(%2 %) args))
