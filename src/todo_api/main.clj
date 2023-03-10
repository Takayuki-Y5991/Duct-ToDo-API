(ns todo-api.main
  (:gen-class)
  (:require [duct.core :as duct]))

(duct/load-hierarchy)

(defn -main [& args]
  (let [keys     (or (duct/parse-keys args) [:duct/daemon])
        profiles [:duct.profile/prod]]
    (-> (duct/resource "todo_api/config.edn")
        (duct/read-config)
        (duct/exec-config profiles keys))
    (System/exit 0)))
