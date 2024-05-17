package com.retail.e_com.utility;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.retail.e_com.model.AccessToken;
import com.retail.e_com.model.RefreshToken;
import com.retail.e_com.repository.AccessTokenRepository;
import com.retail.e_com.repository.RefreshTokenRepository;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class ScheduleJobs {
	private RefreshTokenRepository refreshRepository;
	private AccessTokenRepository accessRepository;

	@Scheduled(fixedDelay = 60*60*1000l)
	public void deleteAllExpiredRefreshTokens() {
		List<RefreshToken> rt=refreshRepository.findAllByExpirationBefore(LocalDateTime.now());
		if(rt!=null)
		{
			if(!rt.isEmpty()) refreshRepository.deleteAll(rt);
		}
	}

	@Scheduled(fixedDelay = 60*60*1000l)
	public void deleteAllExpiredAccessTokens() {
		List<AccessToken> at=accessRepository.findAllByExpirationBefore(LocalDateTime.now());
		if(at!=null)
		{
			if(!at.isEmpty()) accessRepository.deleteAll(at);
		}
	}
}
