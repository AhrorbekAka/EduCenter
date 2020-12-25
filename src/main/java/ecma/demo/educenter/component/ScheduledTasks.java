package ecma.demo.educenter.component;

import ecma.demo.educenter.entity.Group;
import ecma.demo.educenter.entity.TimeTable;
import ecma.demo.educenter.repository.GroupRepository;
import ecma.demo.educenter.repository.TimeTableRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class ScheduledTasks {

    private final GroupRepository groupRepository;
    private final TimeTableRepository timeTableRepository;

    public ScheduledTasks(GroupRepository groupRepository, TimeTableRepository timeTableRepository) {
        this.groupRepository = groupRepository;
        this.timeTableRepository = timeTableRepository;
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void firstDayOfMonth() {
        final Calendar c = Calendar.getInstance();
        List<Group> groupList = groupRepository.findAllByIsPresentOrderByName(true);
        for (Group group : groupList) {
            List<TimeTable> timeTables = group.getTimeTables();
            timeTables.sort(new SortByCreatedAt());
            TimeTable savedTimeTable = timeTableRepository.save(new TimeTable(c.get(Calendar.MONTH), timeTables.get(timeTables.size() - 1).getPaymentForThisMonth()));
            timeTables.add(savedTimeTable);
            group.setTimeTables(timeTables);
        }
        groupRepository.saveAll(groupList);
    }
}

class SortByCreatedAt implements Comparator<TimeTable> {
    @Override
    public int compare(TimeTable a, TimeTable b) {
        return a.getCreatedAt().compareTo(b.getCreatedAt());
    }
}
