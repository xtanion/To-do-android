package com.example.todo.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.todo.data.GroupEntity
import com.example.todo.data.TodoEntity

data class GroupWithTodos(
    @Embedded
    val group: GroupEntity,
    @Relation(
        parentColumn = "groupName",
        entityColumn = "groupName",
        entity = TodoEntity::class
    )
    val todos:List<TodoEntity>
)
