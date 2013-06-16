(ns ^{:author "Uwe Dauernheim <uwe@dauernheim.net>"
      :doc "Core helper functions."}
  clojure.contrib-djui.core
  (:use [let-else :only [let?]]))


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

(defn fix-point
  "Repeatedly call f with data-in until f(data-in) equals data-in."
  [f data-in]
  (let [data-out (f data-in)]
    (if (= data-out data-in)
      data-out ;; Don't return data-in in case meta data was added.
      (recur f data-out))))

;; Inspired by:
;; * https://github.com/cgrand/parsley/blob/master/src/net/cgrand/parsley/util.clj
;; * http://edtsech.github.io/2012/12/and-let.html
(defmacro if-all-let
  "Similar to if-let but supports multiple binding terms which have to be true
  for the binding evaluation to continue."
  ([bindings then]
     `(if-all-let ~bindings ~then nil))
  ([bindings then else]
     (if (seq bindings)
       `(let [test# ~(bindings 1)]
          (if test#
            (let [~(bindings 0) test#]
              (if-let ~(subvec bindings 2) ~then ~else))
            ~else))
       then)))

(defmacro when-all-let
  "Similar to when-let but supports multiple binding terms which have to be true
  for the binding evaluation to continue."
  [bindings & body]
  `(if-all-let ~bindings (do ~@body)))

;; ...alternatively:
#_(defmacro when-all-let
  ""
  [bindings expr]
  (if (seq bindings)
    `(if-let [~(first bindings) ~(second bindings)]
       (and-let ~(drop 2 bindings) ~expr))
    expr))
