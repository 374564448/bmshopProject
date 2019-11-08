package com.banmingi.bmshop.search;

import com.banmingi.bmshop.common.pojo.PageResult;
import com.banmingi.bmshop.search.pojo.Goods;
import com.banmingi.bmshop.search.pojo.SearchRequest;
import com.banmingi.bmshop.search.service.SearchService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @auther 半命i 2019/11/6
 * @description
 */
@RestController
public class controllerSearchController {

    @Autowired
    private SearchService searchService;

    /**
     *
     * @param searchRequest
     * @return
     */
    @PostMapping("page")
    public ResponseEntity<PageResult<Goods>> search(@RequestBody SearchRequest searchRequest) {
        PageResult<Goods> result = this.searchService.search(searchRequest);
        if(result == null || CollectionUtils.isEmpty(result.getItems())) {
            //404
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

}
