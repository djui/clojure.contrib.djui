(ns clojure.contrib.djui.coll-test
  (:use clojure.test)
  (:use clojure.contrib.djui.coll))


(deftest gapmap-test
  (is (= [1 2 1] (gapmap + [0 1 1 0])))
  (is (= [2]     (gapmap + [1 1])))
  (is (= [1]     (gapmap + [1])))
  (is (= []      (gapmap + [])))
  (is (= nil     (gapmap + nil))))

(deftest create-map-test
  (let [a 1, b 2, c 3]
    (is (= {:a 1, :b 2, :c 3} (create-map a b c)))))

(deftest pmapcat-test
  (is (= []          (pmapcat inc nil)))
  (is (= [1 2 3 4 5] (pmapcat reverse [[2 1] [5 4 3]]))))

(deftest sort-maps-by-test
  (is (= []              (sort-maps-by [] :a)))
  (is (= []              (sort-maps-by nil :a)))
  (is (= [{:a 2} {:a 1}] (sort-maps-by [{:a 2} {:a 1}] :b)))
  (is (= [{:a 1} {:a 2}] (sort-maps-by [{:a 2} {:a 1}] :a)))
  (is (= [{:a 1} {:a 2}] (sort-maps-by [{:a 2} {:a 1}] :a :b)))
  (is (= [{:a 1 :b 1} {:a 1 :b 2}]
         (sort-maps-by [{:a 1 :b 2} {:a 1 :b 1}] :a :b))))

(deftest distinct-by-test
  (is (= [{:a 2, :b 7} {:a 4, :b 8}]
         (distinct-by :b [{:a 2, :b 7} {:a 3, :b 7} {:a 4, :b 8}])))
  (is (= [[:a 2] [:b 3]] (distinct-by second [[:a 2] [:b 2] [:b 3]])))
  (is (= (distinct-by identity [1 1 2 3 4 4 5]) (distinct [1 1 2 3 4 4 5]))))

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

(deftest tree-seq+-test
  (is (= [nil] (tree-seq+ sequential? identity nil)))
  (is (= [[]]  (tree-seq+ sequential? identity [])))
  (is (= [[1 [2 [] 3]] 1 [2 [] 3] 2 [] 3]
         (tree-seq+ sequential? identity [1 [2 [] 3]])))
  (is (= [[1 [2 [] 3]] 1 [2 [] 3]]
         (tree-seq+ sequential? identity [1 [2 [] 3]] {:limit 2})))
  (is (= nil
         (tree-seq+ sequential? identity [1 [2 [] 3]] {:limit 0})))
  (is (= (tree-seq+ sequential? identity [1 [2 [] 3]])
         (tree-seq+ sequential? identity [1 [2 [] 3]] {:parallel? true})))
  (is (= (tree-seq  sequential? identity nil)
         (tree-seq+ sequential? identity nil)))
  (is (= (tree-seq  sequential? identity [])
         (tree-seq+ sequential? identity [])))
  (is (= (tree-seq  sequential? identity [1 [2 [] 3]])
         (tree-seq+ sequential? identity [1 [2 [] 3]]))))

(deftest alternate-test
  (is (= [[1 4 7] [2 5 8] [3 6 9]] (alternate 3 (range 1 10))))
  (is (= [[1 4] [2 5] [3 6]]       (alternate 3 (range 1 9))))
  (is (= [[1 2 3 4 5 6 7 8 9]]     (alternate 1 (range 1 10))))
  (is (= []                        (alternate 3 []))))

(deftest pjuxt-test
  (is (= []    ((pjuxt) 1)))
  (is (= [2 0] ((pjuxt inc dec) 1)))
  (is (= ((juxt inc dec) 1) ((pjuxt inc dec) 1)))
  (is (= ((juxt inc dec) 1) ((pjuxt inc dec) 1))))

(deftest |>-test
  (is (= 42      (|> 7 #(* 3 %) (partial * 2))))
  (is (= 4       (|> 4)))
  (is (= []      (|> [])))
  (is (= nil     (|> nil)))
  (is (function? (|> #())))
  (is (thrown? clojure.lang.ArityException (|>))))
