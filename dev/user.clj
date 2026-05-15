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
   [jansenh.clj-go.utilities :as utils]
   [jansenh.clj-go.terminal-board :refer [symbolic-board]]
   [clojure.repl]
   [clojure.tools.namespace.repl :refer [refresh refresh-all]]))

(comment
  ;;; REPL namespace stuff
  ;;  --------------------
  ;;
  (refresh)
  (refresh-all)
  ,)

(comment
  ;;; Utilities stuff
  ;;  ---------------
  ;;
  (utils/numeric-board)
  (utils/patterned-board)
  ,)


(comment
  ;;; Start Graphics game engine
  ;;  --------------------------
  ;;
  (refresh-all) ; useful for reloading states if any changes...
  (clj-go)
  
  ,)

(comment
  ;;; Current board state
  ;;  -------------------
  ;;
  ;;  Captured from the LIVE Graphics board, if a session is  active
  ;;
  
  (def current-board-state {:board (:board @board-state)
                            :current:player (:current-player @board-state)})
  
  (do
    (println "- - - - - - - - - - - - -")
    (println
     (->> (:board current-board-state)
          symbolic-board)))
  
  ,)




(comment
  ;;; Playful board-states
  ;;  --------------------
  ;;
  ;; We can capture live board states from the Graphical Board
  ;; and use in REPL, e.g. for furher exploration with terminal-board
  ;; or create unit-tests on the data
  ;;
  
  (def example-saved-board-state
    [[:black nil nil nil :black nil nil nil :white]
     [:white nil nil nil nil nil nil nil :black]
     [:black nil nil nil nil nil nil nil :white]
     [:white nil nil nil nil nil nil nil :black]
     [:black nil nil nil :black nil nil nil :white]
     [:white nil nil nil nil nil nil nil :black]
     [:black nil nil nil nil nil nil nil :white]
     [:white nil nil nil nil nil nil nil :black]
     [:black nil nil nil :white nil nil nil :white]])
  
  (println
   (->> example-saved-board-state
        symbolic-board))
  
  ,)


