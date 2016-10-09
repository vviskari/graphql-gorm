import graphql.gorm.Todo
import io.cirill.relay.RelayHelpers

class BootStrap {

    def init = { servletContext ->
        Todo t1 = new Todo(title: "Go home", done: false).save()
        println("Created TODO: globalId " + RelayHelpers.toGlobalId("Todo", t1.id.toString()))
        Todo t2 = new Todo(title: "Wake up", done: true).save()
        println("Created TODO: globalId " + RelayHelpers.toGlobalId("Todo", t2.id.toString()))
    }
    def destroy = {
    }
}
