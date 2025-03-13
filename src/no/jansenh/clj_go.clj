(ns no.jansenh.clj-go
  (:require [no.jansenh.clj-go.board :refer [create-frame]])
  (:gen-class))

;;;;
;;;;  Clj-go
;;;;  ------
;;;;
;;;;  Clojure Go Game.
;;;;
;;;;  Henning Jansen 2025  Copyright © henning.jansen@jansenh.no
;;;;
;;;;  Distributed under the GNU General Public License v3.0 as described in the
;;;;  root of this project.
;;;;
(defn -main [& args]
  (create-frame))
