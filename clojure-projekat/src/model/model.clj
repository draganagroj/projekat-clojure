(ns model.model
  (:require [clojure.java.jdbc :as sql]
          [clojure.math.numeric-tower :as math]
          )

  )



(def spec 
  "db properties"
  {:classname "com.mysql.jdbc.Driver"
   :subprotocol "mysql"
   :subname "//127.0.0.1:3306/clojure"
   :user "root"
   :password ""})


(defn all-users []
  "fetching names of all users from database"
   (into [] (flatten(subvec(sql/query spec ["SELECT username FROM user"] :as-arrays? true )1))))


(defn all-users-id []
  "get id of all users"
  (into [] (flatten(subvec(sql/query spec ["SELECT id FROM user"] :as-arrays? true )1)))
  )


(defn create-user [name pass]
  "inserting new user into db"
 (sql/execute! spec ["INSERT INTO user (username,password)  VALUES(?,?) " name pass])
  )

(defn get-comments [id]
  "getting comments for recipe"
  (into  [] (sql/query spec ["SELECT comment.text,user.username  FROM user JOIN comment ON user.id=comment.user JOIN recipe ON recipe.id=comment.recipe WHERE recipe.id = ? " id])
  )
  )

(defn get-recipe [id]
  "getting the recipe"
  (get(into [] (sql/query spec ["SELECT * FROM recipe WHERE recipe.id=?" id]  ))0))
  
(defn get-username[id] 
  "getting the username of user"
    (get(into [] (sql/query spec ["SELECT username FROM user WHERE user.id=?" id]  ))0))
  
  
(defn username-recipe [id]
  "getting the userame of user who wrote recipe"
 (get-username(:user(get-recipe id)))
  )
 
  
(defn recipe-details [id]
  "getting the recipe"
  (conj (get-recipe id) (username-recipe id))
  )

(defn user [name pass]
  "getting the user from db"
 (into  [] (sql/query spec ["SELECT username, password FROM user WHERE username = ? AND password = ?" name pass])
  ))

(defn recipe-id [title]
  "get id of a recipe"
  (:id(get(into  [] (sql/query spec ["SELECT id FROM recipe WHERE title = ? " title])
  )0)))

(defn titles []
  "getting names of recepies"
  (into [] (flatten(subvec(sql/query spec ["SELECT title FROM recipe"] :as-arrays? true )1))))

(defn user-id[name]
  "getting id of a user"
   (:id(get(into  [] (sql/query spec ["SELECT id FROM user WHERE username = ? " name ])
  )0)))

 
(defn insert-commment [comment,user,recipe]
  "inserting comment"
    (sql/execute! spec ["INSERT INTO comment (text, user,recipe) VALUES (?,?,?)" comment (user-id user) recipe ] )
    )
  
    
(defn insert-recipe [title body user]
  "insert recipe"
  (get(into [](sql/execute! spec ["INSERT INTO recipe (title,body,user) VALUES (?,?,?)" title body (user-id user)] )
  )0))


(defn max-id []
  "max recipe id"
 (:max(get(into [](sql/query spec ["SELECT max(id) as max FROM recipe"]))0) )
  )


(defn insert-def-rat []
  "insert default rating of all users for new recipe"
  (for [user (all-users-id)]
  (sql/execute! spec ["INSERT INTO rating (value,user,recipe) VALUES (?,?,?)" 0 user (max-id)])
  ))


(defn update-rating [value user recipe]
  "update rating"
   (sql/execute! spec ["UPDATE rating SET rating.value=? WHERE rating.user=? AND rating.recipe=?" value (user-id user) recipe])
  )


(defn my-recipes [user]
  "get the recipies of logged user"
  (into [](sql/query spec ["SELECT id,title,body from recipe where user=?" (user-id user)]))
  )


(defn rating-def 
  "inserting default rating for all recepies"
  [name]
  (for [t (titles)]
   (sql/execute! spec ["INSERT INTO rating (value, user,recipe) VALUES (?,?,?) " 0 (user-id name) (recipe-id t)] ) )  
  )
  (defn has-rating? [name]
  (into  [] (sql/query spec ["SELECT user.username FROM user JOIN rating ON user.id=rating.user WHERE user.username = ?" name])     
      ))


(defn from-db []
  "getting ratings of users "
  (for [name (all-users)]
  (into [](flatten(subvec (sql/query spec [ "SELECT  rating.value FROM user JOIN rating ON user.id=rating.user JOIN recipe ON recipe.id=rating.recipe WHERE user.username=? ORDER BY recipe.title" name
   ]   :as-arrays? true )1)))))


(defn making-keys []
  "making keys of usernames"
  (map #(keyword %) (all-users)))


(defn user-list []
  "making a list of users and their ratings"
  (zipmap (making-keys)  (from-db)))


(defn sqr
 "square two numbers"
  [x]
  (math/expt x 2))

(defn euclidean-squared-distance
  "computes the Euclidean squared distance between two sequences"
  [a b]
  (reduce + (map (comp sqr -) a b))
  )

(defn euclidean-distance
  "computes the Euclidean distance between two sequences"
  [a b]
  (math/sqrt ( euclidean-squared-distance a b)))


(defn remove-current [user]
  "remove user from map"
  (dissoc (user-list) (keyword user) )
  )


(defn user-vector [user]
  "user ratings"
 ( (keyword user) (user-list))
  )

(defn find-closest
  "find closest user in user map to supplied user"
  [user users]   
   (apply min-key (partial euclidean-distance user) (vals users)))

 
(defn get-closest [user]
  "gets vestor of ratings of most similar users"
  (find-closest (user-vector user) (remove-current user))
  )


(defn get-key [user]
  "get username of most similar user"
(get (get 
       (into [](filter
                 #(= 
                  (get-closest user)
                  (val %)
                  )
              (user-list)
                ))0)0))


(defn return-similar [user]
   "return similar user"
  (into [] (sql/query spec [ "SELECT  user.username, recipe.title, rating.value FROM user JOIN rating ON user.id=rating.user JOIN recipe ON recipe.id=rating.recipe WHERE user.username=?" 
 (name(get-key user)) ]))
  )

(defn liked [user]
  "return liked recipes"
  (vec(filter #(> ( :value %) 0)  (return-similar user))
 ))

(defn return-titles [user]
  "return titiles which the most similar user liked"
  (vec(map  :title (liked user)))
)

(defn best-recipe []
  "return recipes most liked - descending"
  (into [](sql/query spec ["SELECT recipe.id,recipe.title,recipe.body,SUM(rating.value) FROM recipe JOIN rating ON recipe.id=rating.recipe GROUP BY recipe.id"]))
  
  )
 
(defn search-recipe [search]
  "return recipes that contain search word"
    (into [](sql/query spec ["SELECT recipe.id,recipe.title,recipe.body,SUM(rating.value) FROM recipe JOIN rating ON recipe.id=rating.recipe WHERE recipe.title LIKE ? GROUP BY recipe.id" (str "%" search "%")] ))
  
    )


