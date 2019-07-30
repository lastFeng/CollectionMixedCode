/*
 * Copyright 2001-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.springboot.neo4j.neo4j;

import com.springboot.neo4j.config.Neo4jConfig;
import com.springboot.neo4j.dao.Actor;
import com.springboot.neo4j.dao.Movie;
import com.springboot.neo4j.dao.Role;
import com.springboot.neo4j.repository.MovieRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: Guo.Weifeng
 * @version: 1.0
 * @create: 2019/4/28 9:55
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {Neo4jConfig.class})
public class Neo4jTest {
    private static Logger logger = LoggerFactory.getLogger(Neo4jTest.class);

    @Autowired
    private MovieRepository movieRepository;

    @Before
    public void initData() {
        movieRepository.deleteAll();

        Movie movie = new Movie();
        movie.setTitle("The movie");
        movie.setYear("1993-03-01");

        Movie movie1 = new Movie();
        movie1.setTitle("The movie 2");
        movie1.setYear("2003-05-07");

        Movie movie2 = new Movie();
        movie2.setTitle("The movie 3");
        movie2.setYear("2003-10-07");

        Actor actor = new Actor();
        actor.setName("LF");

        Actor actor1 = new Actor();
        actor1.setName("CAM");

        movie.addRole(actor, "LF");
        movie.addRole(actor1, "CAM");
        movieRepository.save(movie);
        Assert.notNull(movie.getId());

        movie1.addRole(actor, "LF");
        movie1.addRole(actor1, "CAM");
        movieRepository.save(movie1);
        Assert.notNull(movie1.getId());

        movie2.addRole(actor, "LF");
        movie2.addRole(actor1, "CAM");
        movieRepository.save(movie2);
        Assert.notNull(movie2.getId());
    }

    @Test
    public void get() {
        Movie movie = movieRepository.findByTitle("The movie");
        Assert.notNull(movie);
        logger.info("====movie=====");
        logger.info("movie:{}, {}", movie.getTitle(), movie.getYear());
        for (Role role : movie.getRoles()) {
            logger.info("===actor:{}, role:{}==", role.getActor(), role.getRole());
        }
    }
}