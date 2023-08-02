package smu.likelion.jikchon.base;


import lombok.Getter;
import java.util.List;
import org.springframework.data.domain.Page;
@Getter
public class PageResult<T> {
    int currentPage; //현재 페이지 번호
    int pageSize; //한 페이지 당 원소 개수
    int totalPage; //전체 페이지 수
    Long totalElements; //모든 페이지에 존재하는 총 원소 수
    List <T> content;
    public PageResult(Page<T> data){
        currentPage=data.getPageable().getPageNumber();
        pageSize=data.getPageable().getPageSize();
        totalPage=data.getTotalPages();
        totalElements=data.getTotalElements();
        content=data.getContent();
    }
    public static PageResult ok(Page data){
        return new PageResult(data);
    }
}
