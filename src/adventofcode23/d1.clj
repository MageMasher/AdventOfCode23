(ns adventofcode23.d1
  "--- Day 1: Trebuchet?! ---
Something is wrong with global snow production, and you've been selected to take a look. The Elves have even given you a map; on it, they've used stars to mark the top fifty locations that are likely to be having problems.

You've been doing this long enough to know that to restore snow operations, you need to check all fifty stars by December 25th.

Collect stars by solving puzzles. Two puzzles will be made available on each day in the Advent calendar; the second puzzle is unlocked when you complete the first. Each puzzle grants one star. Good luck!

You try to ask why they can't just use a weather machine (\"not powerful enough\") and where they're even sending you (\"the sky\") and why your map looks mostly blank (\"you sure ask a lot of questions\") and hang on did you just say the sky (\"of course, where do you think snow comes from\") when you realize that the Elves are already loading you into a trebuchet (\"please hold still, we need to strap you in\").

As they're making the final adjustments, they discover that their calibration document (your puzzle input) has been amended by a very young Elf who was apparently just excited to show off her art skills. Consequently, the Elves are having trouble reading the values on the document.

The newly-improved calibration document consists of lines of text; each line originally contained a specific calibration value that the Elves now need to recover. On each line, the calibration value can be found by combining the first digit and the last digit (in that order) to form a single two-digit number.

For example:
1abc2
pqr3stu8vwx
a1b2c3d4e5f
treb7uchet
  
In this example, the calibration values of these four lines are 12, 38, 15, and 77. Adding these together produces 142.

Consider your entire calibration document. What is the sum of all of the calibration values?"
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn ->n
  "D1-1
  Given a line from the d1-1.txt input file, finds the first and last
  digit (can be the same character!), concatenates the two string
  values, and calls parse-long to return a number.

  Examples:
  1abc2 -> 12
  pqr3stu8vwx -> 38
  a1b2c3d4e5f -> 15
  treb7uchet -> 77"
  [line]
  (-> (re-seq #"[0-9]" line)
      ((juxt first last))
      ((partial apply str))
      parse-long))

(defn word->n
  "D1-2
  Given a line from the d1-1.txt input file, prepares the line for
  processing by `->n` by replacing english number sub-words within the
  line with text containing the number itself.

  Returns the prepared line"
  [line]
  (let [w->n  {"one" "o1e" "two" "t2o" "three" "t3e" "four" "f4r" "five" "f5e" "six" "s6x"
               "seven" "s7n" "eight" "e8t" "nine" "n9e"}]
    (reduce (fn [s [m r]] (str/replace s m r)) line w->n)))

(defn d1
  "Runner for D1-1 and D1-2 puzzles.

  Takes a map of `input`, an input file resource for the puzzle, and
  `d`, a keyword of either `:d1-1` or `:d1-2`, used to dispatch on
  which puzzle to solve.

  Prints the answer to stdout then exits.
  
  TODO: Extract this as a general helper.
  "
  [{:keys [input d]}]
  (with-open [rdr (io/reader (io/resource input))]
    (binding [*in* rdr]
      (->> read-line
           repeatedly
           (take-while some?)
           (map (case d
                  :d1-1 ->n
                  :d1-2 #(-> % word->n ->n)))
           (apply +)
           println))))

(comment
  ;; D1-1 Example
  (def d1-1-lines ["1abc2" "pqr3stu8vwx" "a1b2c3d4e5f" "treb7uchet"])
  (assert (every? true? (map = (map ->n d1-1-lines) [12 38 15 77])))
  (assert (= 142 (apply + (map ->n d1-1-lines))))  
  (d1 {:d :d1-1 :input "d1-1.txt"})
  (assert (= 55712 (d1 {:d :d1-1 :input "d1-1.txt"})))


  ;; D1-2 Example
  (def d1-2-lines ["two1nine" "eightwothree" "abcone2threexyz" "xtwone3four" "4nineeightseven2" "zoneight234" "7pqrstsixteen"])
  (assert (every? true? (map = (map #(-> % word->n ->n) d1-2-lines) [29 83 13 24 42 14 76])))
  (assert (= 281 (apply + (map #(-> % word->n ->n) d1-2-lines))))
  (d1 {:d :d1-2 :input "d1-1.txt"})
  (assert (= 55413 (d1 {:d :d1-2 :input "d1-1.txt"})))
  )






