
.PHONY: repl sql tags test

repl:
	clj -M:nrepl

sql:
	psql -d test_db

test:
	clj -M:test/eftest

lint:
	clj-kondo --lint src test

tags:
	/opt/homebrew/bin/ctags \
		--langdef=Clojure \
		--langmap=Clojure:.clj \
		--langmap=Clojure:+.cljx \
		--langmap=Clojure:+.cljs \
		--regex-clojure="/\([ \t]*create-ns[ \t]+([-[:alnum:]*+!_:\/.?]+)/\1/n,namespace/" \
		--regex-clojure="/\([ \t]*def[ \t]+([-[:alnum:]*+!_:\/.?]+)/\1/d,definition/" \
		--regex-clojure="/\([ \t]*defn[ \t]+([-[:alnum:]*+!_:\/.?]+)/\1/f,function/" \
		--regex-clojure="/\([ \t]*defn-[ \t]+([-[:alnum:]*+!_:\/.?]+)/\1/p,private function/" \
		--regex-clojure="/\([ \t]*defmacro[ \t]+([-[:alnum:]*+!_:\/.?]+)/\1/m,macro/" \
		--regex-clojure="/\([ \t]*definline[ \t]+([-[:alnum:]*+!_:\/.?]+)/\1/i,inline/" \
		--regex-clojure="/\([ \t]*defmulti[ \t]+([-[:alnum:]*+!_:\/.?]+)/\1/a,multimethod definition/" \
		--regex-clojure="/\([ \t]*defmethod[ \t]+([-[:alnum:]*+!_:\/.?]+)/\1/b,multimethod instance/" \
		--regex-clojure="/\([ \t]*defonce[ \t]+([-[:alnum:]*+!_:\/.?]+)/\1/c,definition (once)/" \
		--regex-clojure="/\([ \t]*defstruct[ \t]+([-[:alnum:]*+!_:\/.?]+)/\1/s,struct/" \
		--regex-clojure="/\([ \t]*intern[ \t]+([-[:alnum:]*+!_:\/.?]+)/\1/v,intern/" \
		--regex-clojure="/\([ \t]*ns[ \t]+([-[:alnum:]*+!_:\/.?]+)/\1/n,namespace/" \
		-R src
