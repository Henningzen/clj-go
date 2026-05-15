;;; src/jansenh/clj-go/board.clj  ---  Go game terminal board

;   Copyright (c) Henning Jansen 2025 - 2026
;   The use and distribution terms for this software are covered by the
;   Eclipse Public License 2.0 (https://opensource.org/license/epl-2-0)
;   which can be found in the file LICENSE at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license. You must not remove this notice, or any other,
;   from this software.
;
;; Author:  Henning Jansen - henning.jansen@jansenh.no
;; Date:    September 2025
;; License: Eclipse Public License 2.0 - http://www.eclipse.org/legal/epl-2.0
;;-----------------------------------------------------------------------------

(ns jansenh.clj-go.terminal-board
  (:require [jansenh.clj-go.config :as config :refer [grid-size]]))

;; -----------------------------------------------------------------------------
;; terminal-board
;; --------------
;;
;; String based symbolic representation of a Go board state for REPL and
;; println use.
;;
;; authors:   Henning Jansen, henning.jansen@jansenh.no
;; since:     0.1.1-SNAPSHOT  2025-04-17
;; version:   0.1.2           2026-05-13
;; -----------------------------------------------------------------------------

(def board-size config/grid-size)

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
  ;; NOTE: The annotations skip letter 'I' according to Go game idioms.
  ;;
  ;; NOTE: The board accept three configuration; 9, 13 and 19 intersections.
  ;;
  ;; NOTE: We skip the letter 'I' along with common Go convention,
  ;;       hence the range for n characters span to T.
  ;;
  (condp = board-size
    9  (apply str (interpose " " (map char (seq [\A \B \C \D \E \F \G \H \J]))))
    13 (apply str (interpose " " (map char (seq [\A \B \C \D \E \F \G \H \J \K \L \M \N]))))
    19 (apply str (interpose " " (map char (seq [\A \B \C \D \E \F \G \H \J \K \L \M \N \O \P \Q \R \S \T]))))
    :else nil))

(defn- generate-row-annotations []
  ;; Generate row annotations (n to 1 for a nxn board).
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
                    newline and space."
  [board]
  (let [column-annotations (generate-column-annotations)
        row-annotations (generate-row-annotations)
        padded-row-annotations (pad-row-annotations row-annotations)
        board-with-annotations (map (fn [row-annotation row]
                                      (str row-annotation (apply str (map value-to-symbol row))))
                                    padded-row-annotations
                                    board)]
    (str "   " column-annotations "\n" (apply str (interpose "\n" board-with-annotations)) "\n   " column-annotations)))
