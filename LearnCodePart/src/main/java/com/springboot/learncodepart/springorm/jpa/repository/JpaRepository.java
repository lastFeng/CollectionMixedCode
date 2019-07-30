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
package com.springboot.learncodepart.springorm.jpa.repository;

import com.springboot.learncodepart.domain.Spitter;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.util.List;
import java.util.Optional;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: Guo.Weifeng
 * @version: 1.0
 * @create: 2019/4/26 10:07
 */
@Repository
@Transactional
public class JpaRepository implements SpitterRepository {

    @PersistenceUnit
    private EntityManagerFactory emf;

    public void addSpitter(Spitter spitter) {
        emf.createEntityManager().persist(spitter);  // 创建并使用EntityManager
    }

    public Spitter getSpittersById(Long id) {
        return emf.createEntityManager().find(Spitter.class, id);
    }

    public void saveSpitter(Spitter spitter) {
        emf.createEntityManager().merge(spitter);
    }

    @Override
    public List<Spitter> findAll() {
        return null;
    }

    @Override
    public List<Spitter> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Spitter> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Spitter> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(Spitter entity) {

    }

    @Override
    public void deleteAll(Iterable<? extends Spitter> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends Spitter> S save(S entity) {
        return null;
    }

    @Override
    public <S extends Spitter> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Spitter> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Spitter> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public void deleteInBatch(Iterable<Spitter> entities) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Spitter getOne(Long aLong) {
        return null;
    }

    @Override
    public <S extends Spitter> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Spitter> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Spitter> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Spitter> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Spitter> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Spitter> boolean exists(Example<S> example) {
        return false;
    }
}