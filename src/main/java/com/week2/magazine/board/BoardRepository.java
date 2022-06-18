package com.week2.magazine.board;

import com.week2.magazine.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

//@Repository
public interface BoardRepository extends JpaRepository<Board,Long> {

//    @Modifying
//    @Query(value = "delete from board where id=?", nativeQuery = true)
//    void deleteById(Long id);

}
