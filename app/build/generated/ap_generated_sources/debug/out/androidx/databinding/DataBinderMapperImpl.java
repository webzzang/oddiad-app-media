package androidx.databinding;

public class DataBinderMapperImpl extends MergedDataBinderMapper {
  DataBinderMapperImpl() {
    addMapper(new com.exflyer.oddiad.DataBinderMapperImpl());
  }
}
