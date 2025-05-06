package com.programming.techie.mongo.changelogs;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.github.cloudyrock.mongock.driver.mongodb.springdata.v3.decorator.impl.MongockTemplate;
import com.mongodb.client.MongoDatabase;
import com.programming.techie.mongo.model.Expense;
import com.programming.techie.mongo.model.ExpenseCategory;
import com.programming.techie.mongo.repository.ExpenseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.programming.techie.mongo.model.ExpenseCategory.*;

@Slf4j
@ChangeLog
public class DatabaseChangeLog {

    @ChangeSet(order = "001", id = "populateExpenses2", author = "kgromov")
    public void populateData(ExpenseRepository expenseRepository) {
        log.info("Migration with ExpenseRepository - populating collection {}", Expense.class.getAnnotation(Document.class).value());
        List<Expense> expenseList = new ArrayList<>();
        expenseList.add(createNewExpense("Movie Tickets", ENTERTAINMENT, BigDecimal.valueOf(40)));
        expenseList.add(createNewExpense("Dinner", RESTAURANT, BigDecimal.valueOf(60)));
        expenseList.add(createNewExpense("Netflix", ENTERTAINMENT, BigDecimal.valueOf(10)));
        expenseList.add(createNewExpense("Gym", MISC, BigDecimal.valueOf(20)));
        expenseList.add(createNewExpense("Internet", UTILITIES, BigDecimal.valueOf(30)));

        expenseRepository.insert(expenseList);
    }

    @ChangeSet(order = "002", id = "cleanupExpenses", author = "kgromov")
    public void cleanupData(MongoDatabase database) {
        log.info("Migration with MongoDatabase: {} - dropping collection", database.getName());
        database.getCollection("expense").drop();
    }

    @ChangeSet(order = "003", id = "operationsWithMongoTemplate", author = "kgromov")
    public void mongoTemplate(MongockTemplate mongoTemplate) {
        log.info("Migration with MongoTemplate: {} has the following collections: {}",
                mongoTemplate.getDb().getName(), mongoTemplate.getCollectionNames());
    }

    private Expense createNewExpense(String expenseName, ExpenseCategory expenseCategory, BigDecimal amount) {
        Expense expense = new Expense();
        expense.setExpenseName(expenseName);
        expense.setExpenseAmount(amount);
        expense.setExpenseCategory(expenseCategory);
        return expense;
    }
}
