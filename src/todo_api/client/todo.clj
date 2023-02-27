(ns todo-api.client.todo
  (:require [clojure.java.jdbc :as j]
            [todo-api.utils.parser :refer [stringToSqlDate]]))


(def ^:private fetchAllQuery
  "Select * From todos order by id")

(def ^:private fetchOneQuery
"Select * From todos where id = ?")

(defprotocol IOTodo
 (fetchAll [db])
  (create [db params])
  (fetchOne [db id])
  (delete [db id])
  (change [db id params])
  (changeStatus [db id params])
  )

(extend-protocol IOTodo
  duct.database.sql.Boundary

  (fetchAll [{:keys [spec]}]
    (j/query spec [fetchAllQuery]))

  (create [{:keys [spec]} params]
    (j/insert! spec :todos {:title (:title params)
                            :limit_date (-> (:limit_date params)
                                            (stringToSqlDate))}))

  (fetchOne [{:keys [spec]} id]
    (j/query spec [fetchOneQuery id]))

  (delete [{:keys [spec]} id]
    (j/delete! spec :todos ["id = ?" id]))

  (change [{:keys [spec]} id params]
    (j/update! spec :todos {:title (:title params)
                            :limit_date (-> (:limit_date params)
                                            (stringToSqlDate))} ["id = ?" id]))

  (changeStatus [{:keys [spec]} id params]
    (j/update! spec :todos {:completed (:status params)} ["id = ?" id])))