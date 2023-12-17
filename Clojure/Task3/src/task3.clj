(ns task3)

(defn heavy+ [args]
  (Thread/sleep 10)
  (even? args))

(defn p-filter [pred coll]
  (let [chunk-size (int (Math/ceil (Math/sqrt (count coll))))]
                  (->> (partition-all chunk-size coll)
                       (map #(future (doall (filter pred %))))
                       (doall)
                       (map deref)
                       (flatten))
                  ))
(prn "lib filter")
(time (doall (filter heavy+ (range 100))))
(prn "my filter")
(time (doall (p-filter heavy+ (range 100))))


(require '[clojure.test :refer :all])

(deftest test-primes
  (testing
    (is (= (filter even? (range 10)) (p-filter even? (range 10))))
    (is (= (filter zero? [0, 9, 9, 9, 0, 4, 7, 0]) (p-filter zero? [0, 9, 9, 9, 0, 4, 7, 0])))
    (is (= (filter (fn [x] (= (count x) 1)) ["a" "aa" "b" "n" "f" "lisp" "clojure" "q" ""])
           (p-filter (fn [x] (= (count x) 1)) ["a" "aa" "b" "n" "f" "lisp" "clojure" "q" ""])))))