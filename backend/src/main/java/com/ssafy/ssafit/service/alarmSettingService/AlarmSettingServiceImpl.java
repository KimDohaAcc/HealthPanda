package com.ssafy.ssafit.service.alarmSettingService;

import com.ssafy.ssafit.domain.AlarmSetting;
import com.ssafy.ssafit.repository.AlarmSettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AlarmSettingServiceImpl implements AlarmSettingService {
    private final AlarmSettingRepository alarmSettingRepository;

    @Override
    public AlarmSetting insertAlarmSetting(AlarmSetting alarmSetting) {
        return alarmSettingRepository.save(alarmSetting);
    }

    @Override
    public AlarmSetting findAlarmSettingByUser(String user) {
        return alarmSettingRepository.findAlarmSettingByUser(user);
    }

    @Override
    public AlarmSetting updateAlarmSetting(AlarmSetting alarmSetting) {
        return alarmSettingRepository.save(alarmSetting);
    }

    @Override
    public void removeAlarmSetting(AlarmSetting alarmSetting) {
        alarmSettingRepository.delete(alarmSetting);
    }

}
