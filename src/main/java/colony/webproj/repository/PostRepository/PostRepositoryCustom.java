package colony.webproj.repository.PostRepository;

import colony.webproj.dto.PostDto;
import colony.webproj.dto.PostManageDto;
import colony.webproj.entity.type.SearchType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostRepositoryCustom {
    Page<PostDto> findPostDtoList(SearchType searchType, String searchValue, Boolean answered, String sortBy, Pageable pageable, String categoryName);
    Page<PostManageDto> findPostDtoListAdmin(SearchType searchType, String searchValue, Pageable pageable);
    List<PostDto> findPostDtoNotice();
}
