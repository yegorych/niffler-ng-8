package guru.qa.niffler.service.soap;

import jakarta.annotation.Nonnull;
import guru.qa.jaxb.userdata.Direction;
import guru.qa.jaxb.userdata.PageInfo;
import guru.qa.jaxb.userdata.Sort;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;


public class SoapPageable {
  private final Pageable pageable;

  public SoapPageable(Pageable pageable) {
    this.pageable = pageable;
  }

  public @Nonnull PageInfo pageInfo() {
    PageInfo page = new PageInfo();
    page.setPage(pageable.getPageNumber());
    page.setSize(pageable.getPageSize());
    page.getSort().addAll(sort());
    return page;
  }

  private @Nonnull List<Sort> sort() {
    List<Sort> result = new ArrayList<>();
    if (!pageable.getSort().isEmpty()) {
      for (org.springframework.data.domain.Sort.Order order : pageable.getSort()) {
        Sort sort = new Sort();
        sort.setProperty(order.getProperty());
        sort.setDirection(Direction.valueOf(order.getDirection().name()));
        result.add(sort);
      }
    }
    return result;
  }
}
