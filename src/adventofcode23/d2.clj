(ns adventofcode23.d2
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn line->map
  [line]
  (let [[[_ _ id]] (re-seq #"(\S+) (\d+):" line)
        init-map   {:blue 0 :green 0 :red 0 :id (parse-long id)}
        color-max  (reduce (fn [acc [_ _ num color]]
                             (update acc (keyword color) max (parse-long num)))
                           init-map
                           (re-seq #"((?<num>\d+) (?<color>\w+))" line))]
    (assoc color-max :power (apply * (map color-max [:red :green :blue])))))

(defn run
  [{:keys [input d mr mg mb]}]
  (with-open [rdr (io/reader (io/resource input))]
    (binding [*in* rdr]
      (let [maps (->> read-line repeatedly (take-while some?) (map line->map))
            cube-limits? #(and (<= (:red %) mr) (<= (:green %) mg) (<= (:blue %) mb))
            ret (apply + (case d
                           :d2-1 (->> maps (filter cube-limits?) (map :id))
                           :d2-2 (->> maps (map :power))))]
        (println ret)
        ret))))

(comment
  (def the-line
  "Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green")
  
  (apply * (map (line->map the-line) [:red :green :blue]))

  (assert (= 2256 (run {:input "d2-1.txt" :d :d2-1 :mr 12 :mg 13 :mb 14})))

  (assert (= 74229 (run {:input "d2-1.txt" :d :d2-2}))))
