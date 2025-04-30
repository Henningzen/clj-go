(ns no.jansenh.clj-go.terminal-board)

(defn value-to-symbol
  "Convert a board value to its symbolic representation."
  [value]
  (case value
    :white "○ "
    :black "● "
    nil "  "))

(defn convert-to-symbols
  "Convert each value in the board to its symbolic representation."
  [board]
  (map (fn [row]
         (apply str (map value-to-symbol row)))
       board))

(defn symbolic-board
  "Create a symbolic board 'o - ●' for white, blank (nil) and  black values.
   The intended use is for visualization for debugging in repl or writing
   as text file."
  [board]
  (apply str (interpose "\n" (convert-to-symbols board))))


