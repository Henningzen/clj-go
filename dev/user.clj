;;; user.clj --- Namespace for REPL connected sessions

;   Copyright (c) Henning Jansen 2025 - 2026
;   The use and distribution terms for this software are covered by the
;   Eclipse Public License 2.0 (https://opensource.org/license/epl-2-0)
;   which can be found in the file LICENSE at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license. You must not remove this notice, or any other,
;   from this software.

;; Author:  Henning Jansen - henning.jansen@jansenh.no
;; Date:    September 2025
;; License: Eclipse Public License 2.0 - http://www.eclipse.org/legal/epl-2.0

(ns user
  ^{:author "Henning Jansen"
    :doc    "Namespace for REPL connected sessions"
    :added "0.1.3"}
  (:require
   [jansenh.clj-go.board :refer [board-state]]
   [jansenh.clj-go.core :refer [clj-go]]
   [jansenh.clj-go.engine :refer [update-position-at-board]]
   [jansenh.clj-go.utilities :refer [numeric-board patterned-board]]
   [jansenh.clj-go.terminal-board :refer [symbolic-board]]
   [clojure.pprint :refer [pprint]]
   [clojure.repl :refer [dir-fn]]
   [clojure.tools.namespace.repl :refer [refresh refresh-all]]))

;;; We got magic tooling!
;;  ---------------------
;;  The repl and tools namespaces are our best allied
;;

(defn explore-namespace
  "Demonstrates the use or the 'clojure.repl' namespace
   Takes on namespace argument. The repl function 'dir-fn' returns a sorted
   seq of all symbols in the namespace.

   NOTE: ('dir-fn' has an equivalent 'dir'
   printing a sorted directory of a given namespace.)

   Returns: nil (side-effect system out)."
  [ns]
  (->> ns
       dir-fn
       pprint))

(comment
  (explore-namespace 'jansenh.clj-go.board)
  ,)

;;; Graphics game engine, Current board state and REPL tooling
;;  ----------------------------------------------------------
;;
;;  The following is a set of REPL function for starting the Graphics game
;;  board from a REPL, capture game-state from the live Graphics game board
;;  (if a session is active).
;;  With the game state data map, we can invoke that state in our
;;  'jansenh.clj-go.terminal-board' namespace and explore with stuff in
;;  'utilities' namespace, or carry stuff over in the 'user.dev' namespace.
;;
;;  NOTE: See '(comment) section below for more useful tools
;;
;;  Happy REPL'ing!
;;

(defn start-game
  "Starts the Graphics game engine in ns 'board'.
   TODO: Add a 'stop-game' function asap, and compound a 'restart-game'
   Returns: Javax.Swing object (active Graphics board, internal game state).  "
  []
  (clj-go))


(defn current-board-state
  "Current board state, dereffed from atom in ns 'board.
   Returns: Game-state map.  "
  []
  {:board (:board @board-state)
   :current-player (:current-player @board-state)})


(defn println-current-board-state->
  "Printline utility for current board state.
   Note:    Not pure function, and not very flexible; strongly tied
            to the game state map from ns 'board.
   Returns: nil (dirty with side-effects, our imperative shell).  "
  [m]
  (println "\n" (symbolic-board (:board (m)))
             (str "\nPlayer: " (name (:current-player (m))) "\n")))


(defn println-point->terminal-board
  "Set any point(s).
   Provided a point map '{:player :black, :x-pos 0, :y-pos 0}' and
   a board map, this function will println a terminal-board.
   Returns: nil (side-effect 'println'. "
  [stone board]
  (->> board
       (update-position-at-board stone)
       (symbolic-board)
       (println)))


;;; ----------------------------------------------------------------------------
;;  Useful REPL in-editor tooling
;;

(comment

  ;; Start Graphics game engine
  ;;
  (refresh-all)
  (clj-go)

  ;; REPL stuff
  ;;
  (refresh)
  (refresh-all)

  ;; Utilities stuff
  ;;
  (numeric-board 19)
  (patterned-board 19)

  ;; ----
  )

(comment

  ;;; Playful board-states
  ;;  --------------------
  ;;
  ;; We de-ref the Graphics board atom in the 'current-board-state' function
  ;; above, and here we throw that game state map onto a REPL tool function,
  ;; 'terminal-board' for pretty-printing raw game state data.
  ;; Being a Clojure developer is the next best thing ever, after simplu being
  ;; alive!
  ;;

  (do
    (println "- - - - - - - - - - - - -")
    (println
     (->> (:board (current-board-state))
          symbolic-board))
    (println (str "Current player: "
                  (name (:current-player (current-board-state))))))

  ;;; Raw go board datastructure example
  ;;  ----------------------------------
  ;;
  ;; We can capture live board states from the Graphical Board
  ;; and use in REPL, e.g. for furher exploration with terminal-board
  ;; or create unit-tests on the data
  ;;

  (println
   (->> [[:black nil nil nil :black nil nil nil :white]
         [:white nil nil nil nil nil nil nil :black]
         [:black nil nil nil nil nil nil nil :white]
         [:white nil nil nil nil nil nil nil :black]
         [:black nil nil nil :black nil nil nil :white]
         [:white nil nil nil nil nil nil nil :black]
         [:black nil nil nil nil nil nil nil :white]
         [:white nil nil nil nil nil nil nil :black]
         [:black nil nil nil :white nil nil nil :white]]
        symbolic-board))

  ;; --->
  )
