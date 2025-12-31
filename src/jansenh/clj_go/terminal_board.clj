;; -----------------------------------------------------------------------------
;; File: src/jansenh/clj_go/terminal_board.clj
;; Author: Henning Jansen - henning.jansen@jansenh.no
;; Copyright: (c) 2025
;; License: Distributed under the GNU General Public License v3.0
;; as described in the root of this project.
;; -----------------------------------------------------------------------------

(ns jansenh.clj-go.terminal-board)

;; -----------------------------------------------------------------------------
;; terminal-board
;; --------------
;;
;; String based symbolic representation of a Go board state for REPL and
;; println use.
;;
;; authors:   Henning Jansen            henning.jansen@jansenh.no
;; since:     0.1.1-SNAPSHOT            2025-04-17
;; version:   0.1.1-SNAPSHOT
;; -----------------------------------------------------------------------------

(def board-size 19)

(defn- value-to-symbol [value]
  ;; Convert a board value to its symbolic representation.
  ;; (replace the blanks with "· " for stylish dotted grid....)
  (case value
    :white "○ "
    :black "● "
    nil "  "))

(defn- convert-to-symbols [board]
  ;; Convert each value in the board to its symbolic representation.
  ;; The board is a vector of vectors, hence the implicit board -> row mapping
  (map (fn [row] (apply str (map value-to-symbol row))) board))

#_(defn- generate-column-annotations []
  ;; Generate column annotations (A to S for a 19x19 board)
  ;; separated by one space.
  (apply str (interpose " " (map char (range (int \A) (+ (int \A) board-size))))))

(defn- generate-column-annotations []
  ;; Generate column annotations (A to T for a 19x19 board).
  ;; separated by one space.
  ;;
  ;; NOTE: We skip the letter 'I' along with common Go convention,
  ;;       hence the range for 19 characters span to T.
  (apply str (interpose " " (map char (seq [\A \B \C \D \E \F \G \H \J \K \L \M \N \O \P \Q \R \S \T])))))

(defn- generate-row-annotations []
  ;; Generate row annotations (19 to 1 for a 19x19 board).
  (apply str (interpose "\n" (reverse (range 1 (inc board-size))))))

(defn- pad-row-annotations [row-annotations]
  ;; Pad row annotations with spaces to align with the board.
  (map (fn [row] (str (format "%2d" (Integer/parseInt row)) " "))
       (clojure.string/split-lines row-annotations)))

(defn symbolic-board
  "Create a symbolic board. 

   The board has row and column annotations. The string is intended 
   for visualization in REPL or text file.
   
   Arguments: board (reference vector structure in core namespace)

   Returns: String, padded with row/col annotations, formatted with
                    newline and space.
   "
  [board]
  (let [column-annotations (generate-column-annotations)
        row-annotations (generate-row-annotations)
        padded-row-annotations (pad-row-annotations row-annotations)
        board-with-annotations (map (fn [row-annotation row]
                                      (str row-annotation (apply str (map value-to-symbol row))))
                                    padded-row-annotations
                                    board)]
    (str "   " column-annotations "\n" (apply str (interpose "\n" board-with-annotations)) "\n   " column-annotations)))
