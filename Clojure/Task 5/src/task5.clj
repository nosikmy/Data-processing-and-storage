(ns task5)

(def philosophers-number 5)
(def thinking 1000)
(def eating 1000)
(def spaghetti-number 5)

(def forks (take philosophers-number (repeatedly #(ref 0 :validator (fn [x] (and (<= 0 x) (<= x 1)))))))
(def transaction-restarts (atom 0))

(defn philosopher [id left-fork right-fork]
  (dotimes [spaghetti spaghetti-number]
    (do
      (println (str "The philosopher " id " is thinking"))
      (Thread/sleep thinking)
      (dosync
        (swap! transaction-restarts inc)
        (alter left-fork inc)
        (println (str "The philosopher " id " get left fork"))
        (alter right-fork inc)
        (println (str "The philosopher " id " get right fork"))
        (Thread/sleep eating)
        (println (str "The philosopher " id " finish eating " (- spaghetti-number (+ spaghetti 1)) " spaghetti left"))
        (alter right-fork dec)
        (println (str "The philosopher " id " put right fork"))
        (alter left-fork dec)
        (println (str "The philosopher " id " put left fork"))
        (swap! transaction-restarts dec)))))

(def philosophers
  (map #(new Thread
             (fn []
               (philosopher %
                            (nth forks %)
                            (nth forks (mod (inc %) philosophers-number)))))
       (range philosophers-number)))

(defn eating-philosophers []
  (do
    (doall (map #(.start %) philosophers))
    (doall (map #(.join %) philosophers))))

(time (eating-philosophers))
(println "Transaction restarts: " (deref transaction-restarts))