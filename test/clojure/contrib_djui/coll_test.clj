(ns clojure.contrib-djui.coll-test
  (:use clojure.test)
  (:use clojure.contrib-djui.coll))


(deftest gapmap-test
  (is (= [1 2 1] (gapmap + [0 1 1 0])))
  (is (= [2]     (gapmap + [1 1])))
  (is (= [1]     (gapmap + [1])))
  (is (= []      (gapmap + [])))
  (is (= nil     (gapmap + nil))))

(deftest create-map-test
  (let [a 1, b 2, c 3]
    (is (= {:a 1, :b 2, :c 3} (create-map a b c)))))

(deftest unit-test
  (is (= 1   (unit [1])))
  (is (= nil (unit [1 2])))
  (is (= nil (unit [nil])))
  (is (= nil (unit [])))
  (is (= nil (unit nil))))

(deftest any?-test
  (is (true?  (any? [true false nil])))
  (is (true?  (any? [1 false nil])))
  (is (true?  (any? #(= 1 %) [1 true false nil])))
  (is (true?  (any? true? [1 true false nil])))
  (is (false? (any? [false nil])))
  (is (false? (any? [nil])))
  (is (false? (any? [])))
  (is (false? (any? nil)))
  (is (false? (any? #(= 2 %) [1 true false nil])))
  (is (false? (any? true? [1 false nil])))
  (is (false? (any? true? [])))
  (is (false? (any? true? nil))))

(deftest alternate-test
  (is (= [[1 4 7] [2 5 8] [3 6 9]] (alternate 3 (range 1 10))))
  (is (= [[1 4] [2 5] [3 6]] (alternate 3 (range 1 9))))
  (is (= [[1 2 3 4 5 6 7 8 9]] (alternate 1 (range 1 10))))
  (is (= [] (alternate 3 []))))

(deftest |>-test
  (is (= 42 (|> 7 #(* 3 %) (partial * 2))))
  (is (= 4 (|> 4)))
  (is (= [] (|> [])))
  (is (= nil (|> nil)))
  (is (function? (|> #())))
  (is (thrown? clojure.lang.ArityException (|>))))
