(ns ^{:author "Uwe Dauernheim <uwe@dauernheim.net>"
      :doc "Core helper functions."}
  clojure.contrib-djui.core
  (:use [let-else :only [let?]]))


;; Courtesy https://github.com/clojure/clojure/blob/master/src/clj/clojure/core.clj
(defmacro assert-args
  {:added "1.4"}
  [& pairs]
  `(do (when-not ~(first pairs)
         (throw (IllegalArgumentException.
                 (str (first ~'&form) " requires " ~(second pairs) " in " ~'*ns* ":" (:line (meta ~'&form))))))
       ~(let [more (nnext pairs)]
          (when more
            (list* `assert-args more)))))

(defn fixpoint
  "Repeatedly call f with data-in until f(data-in) equals data-in."
  {:added "1.4"}
  [f data-in]
  (let [data-out (f data-in)]
    (if (= data-out data-in)
      data-out ;; Don't return data-in in case meta data was added.
      (recur f data-out))))

(defn fargs
  "Return a function that takes a fn f yielding (apply f args). While partial is
  left-associative, fargs is right-associative."
  {:added "1.5"} [& args]
  (fn [f] (apply f args)))

(defn keep-if
  "Verify that (pre x) is true and return x, otherwise nil. If f is given,
  return (f x). If post is given, verify that (post (f x)) is true and return
  (f x), otherwise nil.

  Example:
  (keep-if 5 even?) => 5
  (keep-if 5 even? dec) => 4
  (keep-if 5 even? dec even?) => nil"
  {:added "1.4"}
  ([x pre] (when (pre x) x))
  ([x pre f] (when (pre x) (f x)))
  ([x pre f post] (when (pre x) (keep-if (f x) post))))

;; Courtesy https://github.com/cgrand/parsley/blob/master/src/net/cgrand/parsley/util.clj
;; Courtesy http://edtsech.github.io/2012/12/and-let.html
(defmacro if-all-let
  "Similar to if-let but supports multiple binding terms which have to be true
  for the binding evaluation to continue.

  Example:
    (if-all-let [a 1
                 b (dec a)
                 c (pos? b)]
      [a b c]
      \"failed\") => \"failed\""
  {:added "1.4"}
  ([bindings then]
     `(if-all-let ~bindings ~then nil))
  ([bindings then else & oldform]
     (assert-args
      (vector? bindings) "a vector for its binding"
      (nil? oldform) "1 or 2 forms after binding vector"
      (even? (count bindings)) "a multiple of 2 binding terms")
     (if (seq bindings)
       ;; This implementation mimiques the original implementation as close as
       ;; possible. For a shorter but analogues implementation see below.
       (let [form (bindings 0) tst (bindings 1)]
         `(let [temp# ~tst]
            (if temp#
              (let [~form temp#]
                (if-all-let ~(subvec bindings 2) ~then ~else))
              ~else)))
       ;; `(if-let [~(first bindings) ~(second bindings)]
       ;;    (if-all-let ~(drop 2 bindings) ~then ~else)
       ;;    ~else)
       then)))

(defmacro when-all-let
  "Similar to when-let but supports multiple binding terms which have to be true
  for the binding evaluation to continue. Same as if-all-let without the else case.

  Example:
    (when-all-let [a 2
                   b (dec a)
                   c (pos? b)]
      [a b c]) => [2 1 true]"
  {:added "1.4"}
  [bindings & body]
  `(if-all-let ~bindings (do ~@body)))

;; Courtesy https://github.com/flatland/useful/blob/develop/src/flatland/useful/experimental.clj
(defmacro let-if
  "Choose a set of bindings based on the result of a conditional test.

  Example:
   (let-if (even? a)
           [b (bar 1 2 3) (baz 1 2 3)
            c (foo 1)     (foo 3)]
     (println (combine b c)))"
  {:added "1.4"}
  [test bindings & forms]
  (letfn [(alternate [n coll]
            (let [x (partition n coll)]
              (if (empty? x) x
                  (apply map vector x))))]
    (let [[names thens elses] (alternate 3 bindings)]
      `(if ~test
         (let [~@(interleave names thens)] ~@forms)
         (let [~@(interleave names elses)] ~@forms)))))

;; Courtesy https://github.com/flatland/useful/blob/develop/src/flatland/useful/seq.clj
(defmacro lazy
  "Return a lazy sequence of the passed-in expressions. Each will be evaluated
  only if necessary."
  {:added "1.4"}
  [& exprs]
  `(map force (list ~@(for [expr exprs] `(delay ~expr)))))

(defmacro ignorantly
  "Execute f and return its result. In case an exception is thrown, catch it
  and return nil."
  {:added "1.4"}
  [& exprs]
  `(try ~@exprs (catch Exception _#)))

(defmacro unless
  "Same as if, but with negated predicate check."
  [test body]
  `(if (not ~test) ~body))
