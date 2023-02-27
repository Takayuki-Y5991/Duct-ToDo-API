(ns todo-api.utils.parser)

(defn stringToSqlDate [str]
  (java.sql.Date/valueOf str))