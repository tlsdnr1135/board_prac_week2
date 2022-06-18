package com.week2.magazine.board;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.week2.magazine.Security.UserDetailsImpl;
import com.week2.magazine.account.Account;
import com.week2.magazine.account.AccountRepository;
import com.week2.magazine.board.dto.BoardDto;
import com.week2.magazine.board.dto.BoardResponseDto;
import com.week2.magazine.board.entity.Board;
import com.week2.magazine.board.entity.Likes;
import com.week2.magazine.s3.AwsS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class BoardController {
    private static final String BASE_DIR = "images";
    private final BoardRepository boardRepository;
    private final AmazonS3Client amazonS3Client;
    private final AccountRepository accountRepository;
    private final AmazonS3 amazonS3;

    private final LikesRepository likesRepository;

    private final AwsS3Service awsS3Service;

    @GetMapping("/boards")
    public ResponseEntity<?> boardList(){
//        List<Board> asd = boardRepository.findAll(Sort.by(Sort.Direction.DESC,"id"));
        List<Board> s = boardRepository.findAll();
        List<BoardResponseDto> boardResponseDtos = s.stream().map(BoardResponseDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(boardResponseDtos);
    }

    @PostMapping("/boards")
    public ResponseEntity<?> addBoardList(BoardDto boardDto, @RequestPart(required = false) List<MultipartFile> multipart, @AuthenticationPrincipal Authentication auth){
        Object authentication = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(authentication);
        UserDetailsImpl principalDetail = (UserDetailsImpl)authentication;
        String username = principalDetail.getUsername();
        Long longs = principalDetail.getId();

        System.out.println(" 유저 아이디 "+longs);
        Account account = accountRepository.findById(longs).orElseThrow(
                ()-> new IllegalArgumentException("아이디가 없음")
        );

        List<String> list = awsS3Service.uploadFile(multipart);
        boardDto.setImg_Url(list.get(0));
        boardDto.setUserId(longs);

        Board board = boardDto.addBoardList(account);
        boardRepository.save(board);
        return ResponseEntity.ok().body(board);
    }

    //게시판 수정
    @Transactional
    @PutMapping("/boards/{userId}")
    public ResponseEntity<?> updateBoardList(@PathVariable("userId") Long id, BoardDto boardDto, @RequestPart("img") List<MultipartFile> multipartFile){
        Board board = boardRepository.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("ㄴㅇ")
        );
        System.out.println(boardDto);
        List<String> list = awsS3Service.uploadFile(multipartFile);

        board.boardUpdate(boardDto,list);

        return ResponseEntity.ok().body(board);
    }

    @DeleteMapping ("/boards/{id}")
    public ResponseEntity<?> deleteBoardList(@PathVariable("id") Long id){
//        Board board = boardRepository.findById(id).orElseThrow(
//                ()-> new IllegalArgumentException()
//        );
//        System.out.println(board.getId());
//        int sd = boardRepository.deleteById(id);
        boardRepository.deleteById(id);
        return ResponseEntity.ok().body(id);
    }


    @PostMapping("/boards/{id}/likes")
    public ResponseEntity<?> addLikes(@PathVariable("id") Long sd,@AuthenticationPrincipal UserDetailsImpl auth){
        //누르면 보드에 추가
        Board board = boardRepository.findById(sd).orElseThrow(
                ()-> new IllegalArgumentException("없음")
        );
        Account account = accountRepository.findById(auth.getId()).orElseThrow(
                ()-> new IllegalArgumentException("없음")
        );
        Likes likes = Likes.builder()
                .account(account)
                .board(board)
                .build();

        likesRepository.save(likes);

        return ResponseEntity.ok().body(likes);
    }

    @DeleteMapping("/boards/{id}/likes")
    public ResponseEntity<?> deleteLikes(@PathVariable("id") Long sd){
        likesRepository.deleteById(sd);
        return ResponseEntity.ok().body(sd);
    }

}