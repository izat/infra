package infrastructure.bean;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PagedList<T> {
    private List<T> records;        //要查询页的数据
    private Integer pageNumber;     //要查询的第几页
    private Integer pageSize;       //每页大小
    private Integer pageCount;      //总共多少页
    private Integer recordCount;    //总共记录条数

    public PagedList(List<T> records) {
        this.records = records;
    }

    public <O> PagedList(List<O> records, Function<? super O, ? extends T> mapper) {
        this(records.stream().map(mapper).collect(Collectors.toList()));
    }

    public PagedList(List<T> records, Integer pageNumber, Integer pageSize, Integer recordCount) {
        this.records = records;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.recordCount = recordCount;

        if (recordCount != null && pageSize != null && pageSize != 0) {
            this.pageCount = (recordCount - 1) / pageSize + 1;
        }
    }

    public <O> PagedList(List<O> records, Function<? super O, ? extends T> mapper, Integer pageNumber, Integer pageSize, Integer recordCount) {
        this(records.stream().map(mapper).collect(Collectors.toList()), pageNumber, pageSize, recordCount);
    }

    public <R> PagedList<R> map(Function<? super T, ? extends R> mapper) {
        return new PagedList<>(records, mapper, pageNumber, pageSize, recordCount);
    }

    public List<T> getRecords() {
        return records;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public Integer getRecordCount() {
        return recordCount;
    }
}
