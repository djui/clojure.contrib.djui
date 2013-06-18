(ns clojure.contrib-djui.core-test
  (:use clojure.test)
  (:use clojure.contrib-djui.core))

(deftest fixpoint-test
  (is (= 10 (fixpoint #(if (= % 10) % (inc %)) 0))))

(deftest keep-if-test
  (is (= [1] (keep-if [1]   (fn [x] (= 1 (count x))))))
  (is (= 1   (keep-if [1]   (fn [x] (= 1 (count x))) first)))
  (is (= 1   (keep-if [1]   (fn [x] (= 1 (count x))) first pos?)))
  (is (nil?  (keep-if [1 2] (fn [x] (= 1 (count x))))))
  (is (nil?  (keep-if [1 2] (fn [x] (= 1 (count x))) first)))
  (is (nil?  (keep-if [1 2] (fn [x] (= 1 (count x))) first pos?))))

(deftest if-all-let-test
  (is (= [2 1 true] (if-all-let [a 2, b (dec a), c (pos? b)] [a b c])))
  (is (= [2 1 true] (if-all-let [a 2, b (dec a), c (pos? b)] [a b c] "else")))
  (is (= "else"     (if-all-let [a 1, b (dec a), c (pos? b)] [a b c] "else")))
  (is (nil?         (if-all-let [a 1, b (dec a), c (pos? b)] [a b c])))
  (is (= "foo"      (if-all-let [] "foo")))
  (is (thrown-with-msg? IllegalArgumentException
                        #"if-all-let requires a multiple of 2 binding terms"
                        (if-all-let [a 1, b (dec a), c] [a b c] "else"))))

(deftest when-all-let-test
  (is (= [2 1 true] (if-all-let [a 2, b (dec a), c (pos? b)] [a b c])))
  (is (nil?         (if-all-let [a 1, b (dec a), c (pos? b)] [a b c])))
  (is (= "foo"      (if-all-let [] "foo")))
  (is (thrown-with-msg? IllegalArgumentException
                        #"if-all-let requires a multiple of 2 binding terms"
                        (if-all-let [a 1, b (dec a), c] [a b c]))))

(deftest let-if-test
  (is (= [1 2 true]  (let-if (even? 0) [a 1 2, b 2 3, c true false] [a b c])))
  (is (= [2 3 false] (let-if (even? 1) [a 1 2, b 2 3, c true false] [a b c])))
  (is (nil?          (let-if (even? 1) [a 1 2, b 2 3, c true false])))
  (is (nil?          (let-if (even? 1) [a 1])))
  (is (nil?          (let-if (even? 0) [a]))))

#_
(deftest lazy-test
  (is ...))

(deftest ignorantly-test
  (is (= 1  (ignorantly (/ 1 1))))
  (is (= 2  (ignorantly (/ 1 1) (/ 2 1))))
  (is (nil? (ignorantly (/ 1 0))))
  (is (nil? (ignorantly (/ 1 0) (/ 1 1))))
  (is (nil? (ignorantly (/ 1 1) (/ 1 0)))))
