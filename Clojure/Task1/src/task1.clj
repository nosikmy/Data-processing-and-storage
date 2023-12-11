(ns task1)
(require '[clojure.string :as str])

(def alphabet ["a" "b" "c"])
(def n 3)

(defn generate-strings [alphabet n]
  (reduce
    (fn [acc _]
      (for [s acc
            char alphabet]
        (if (not (str/ends-with? s char))
          (str s char)
          s)))
    [""]
    (range n)))

(def result (filter (fn [x] (= (count x) 3)) (generate-strings alphabet n)))
(prn result)
