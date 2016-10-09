package graphql.gorm

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class TodoController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Todo.list(params), model:[todoCount: Todo.count()]
    }

    def show(Todo todo) {
        respond todo
    }

    def create() {
        respond new Todo(params)
    }

    @Transactional
    def save(Todo todo) {
        if (todo == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (todo.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond todo.errors, view:'create'
            return
        }

        todo.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'todo.label', default: 'Todo'), todo.id])
                redirect todo
            }
            '*' { respond todo, [status: CREATED] }
        }
    }

    def edit(Todo todo) {
        respond todo
    }

    @Transactional
    def update(Todo todo) {
        if (todo == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (todo.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond todo.errors, view:'edit'
            return
        }

        todo.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'todo.label', default: 'Todo'), todo.id])
                redirect todo
            }
            '*'{ respond todo, [status: OK] }
        }
    }

    @Transactional
    def delete(Todo todo) {

        if (todo == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        todo.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'todo.label', default: 'Todo'), todo.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'todo.label', default: 'Todo'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
