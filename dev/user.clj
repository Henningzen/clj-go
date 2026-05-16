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
    :added "0.1.2"}
  (:require
   [jansenh.clj-go.board :refer [board-state]]
   [jansenh.clj-go.core :refer [clj-go]]
   [jansenh.clj-go.utilities :refer [numeric-board patterned-board]]
   [jansenh.clj-go.terminal-board :refer [symbolic-board]]
   [clojure.tools.namespace.repl :refer [refresh refresh-all]]))

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
   NOTE: A 'repl/refresh-all' is invoked, all accumulated state will be wiped!
   Returns: Javax.Swing object.  "
  []
  (do
    (refresh-all)
    (clj-go)))


(defn current-board-state
  "Current board state, dereffed from atom in ns 'board.
   '{:board [ [][] ...[] ] :current-player :black}'
   Returns: Game-state map.  "
  []
  {:board (:board @board-state)
   :current-player (:current-player @board-state)})


(defn println-current-board-state
  "Printline utility. Not pure function, and not very flexible; strongly tied
   to the game state map from ns 'board.
   Returns: nil (dirty with side-effects, our imperative shell).  "
  [m]
  (do
    (println "- - - - - - - - - - - - -")
    (println (symbolic-board (:board (m))))
    (println (str "Player: " (name (:current-player (m)))))))

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
  (numeric-board)
  (patterned-board)

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
