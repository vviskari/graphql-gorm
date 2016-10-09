package graphql.gorm

import io.cirill.relay.annotation.*

@RelayType(description='Something I have todo')
class Todo {
    @RelayField(description='What todo')
    String title

    @RelayField(description='Is it already done')
    boolean done

    @RelayQuery
    static one = {
        io.cirill.relay.dsl.GQLFieldSpec.field {
            name "justReturnsOne"
            type graphql.Scalars.GraphQLInt
            dataFetcher { env -> 1 } // just returns 1
        }
    }
}
