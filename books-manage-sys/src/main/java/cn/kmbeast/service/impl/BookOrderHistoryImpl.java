package cn.kmbeast.service.impl;

import cn.kmbeast.context.LocalThreadHolder;
import cn.kmbeast.mapper.BookMapper;
import cn.kmbeast.mapper.BookOrderHistoryMapper;
import cn.kmbeast.pojo.api.ApiResult;
import cn.kmbeast.pojo.api.PageResult;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.BookOrderHistoryQueryDto;
import cn.kmbeast.pojo.dto.query.extend.BookQueryDto;
import cn.kmbeast.pojo.entity.BookOrderHistory;
import cn.kmbeast.pojo.vo.BookOrderHistoryVO;
import cn.kmbeast.pojo.vo.BookVO;
import cn.kmbeast.service.BookOrderHistoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 书籍预约历史业务逻辑实现
 */
@Service
public class BookOrderHistoryImpl implements BookOrderHistoryService {
    @Resource
    private BookOrderHistoryMapper bookOrderHistoryMapper;
    @Resource
    private BookMapper bookMapper;

    /**
     * 书籍预约历史新增（借书）
     *
     * @param bookOrderHistory 参数
     * @return Result<Void>
     */
    @Override
    public Result<Void> save(BookOrderHistory bookOrderHistory) {
        // 是不是已经借了这本书
        BookOrderHistoryQueryDto dto = new BookOrderHistoryQueryDto();
        dto.setUserId(LocalThreadHolder.getUserId());
        dto.setBookId(bookOrderHistory.getBookId());
        List<BookOrderHistoryVO> bookOrderHistoryVOS = bookOrderHistoryMapper.query(dto);
        for (BookOrderHistoryVO bookOrderHistoryVO : bookOrderHistoryVOS) {
            if (!bookOrderHistoryVO.getIsReturn()) {
                return ApiResult.error("该书籍未归还");
            }
        }
        // 借的书，是不是有足够的数量
        BookQueryDto bookQueryDto = new BookQueryDto();
        bookQueryDto.setId(bookOrderHistory.getBookId());
        List<BookVO> bookVOS = bookMapper.query(bookQueryDto);
        if (bookVOS.isEmpty()) {
            return ApiResult.error("书籍信息找不到");
        }
        BookVO bookVO = bookVOS.get(0);
        if (bookVO.getNum() - bookOrderHistory.getDeadlineNum() < 0) {
            return ApiResult.error("数量不足");
        }
        // 设置用户ID
        bookOrderHistory.setUserId(LocalThreadHolder.getUserId());
        // 未归还
        bookOrderHistory.setIsReturn(false);
        bookOrderHistory.setCreateTime(LocalDateTime.now());
        bookOrderHistoryMapper.save(bookOrderHistory);
        return ApiResult.success();
    }

    /**
     * 书籍预约历史删除
     *
     * @param ids 参数
     * @return Result<Void>
     */
    @Override
    public Result<Void> batchDelete(List<Integer> ids) {
        bookOrderHistoryMapper.batchDelete(ids);
        return ApiResult.success();
    }

    /**
     * 书籍预约历史修改
     *
     * @param bookOrderHistory 参数
     * @return Result<Void>
     */
    @Override
    public Result<Void> update(BookOrderHistory bookOrderHistory) {
        bookOrderHistoryMapper.update(bookOrderHistory);
        return ApiResult.success();
    }

    /**
     * 书籍预约历史查询
     *
     * @param dto 查询参数
     * @return Result<List < BookOrderHistoryVO>>
     */
    @Override
    public Result<List<BookOrderHistoryVO>> query(BookOrderHistoryQueryDto dto) {
        List<BookOrderHistoryVO> noticeList = bookOrderHistoryMapper.query(dto);
        Integer totalCount = bookOrderHistoryMapper.queryCount(dto);
        return PageResult.success(noticeList, totalCount);
    }


}
