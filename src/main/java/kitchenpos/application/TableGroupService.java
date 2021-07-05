package kitchenpos.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.domain.TableGroupRepository;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository, final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final List<OrderTable> orderTables = getOrderTables(tableGroupRequest);

        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), orderTables);

        return TableGroupResponse.of(tableGroupRepository.save(tableGroup));
    }

    private List<OrderTable> getOrderTables(TableGroupRequest tableGroupRequest) {
        List<OrderTable> orderTables = tableGroupRequest.getOrderTables()
                .stream()
                .map(orderTableRequest -> findByOrderTable(orderTableRequest.getId()))
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }

        return orderTables;
    }

    private OrderTable findByOrderTable(Long id) {
        return orderTableRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 테이블입니다."));
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {

        final List<OrderTable> orderTables = orderTableRepository.findByTableGroupId(tableGroupId);

        List<Order> orders = orderRepository.findByOrderTableIn(orderTables);

        orders.stream()
                .forEach(Order::ungroupValidation);
        orderTables.stream()
                .forEach(OrderTable::ungroup);
    }
}
