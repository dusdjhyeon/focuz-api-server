package dcom.focuz.api.global.config.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import dcom.focuz.api.domain.user.Role;
import dcom.focuz.api.domain.user.User;
import dcom.focuz.api.domain.user.repository.UserRepository;
import dcom.focuz.api.global.config.security.Token;
import dcom.focuz.api.global.config.security.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final TokenService tokenService;
    private final UserRepository userRepository;

    @Value("${website.url}")
    private String websiteURL;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();  // CustomOAuth2UserService에서 넘어옴.
        String email = (String) oAuth2User.getAttribute("email");// 그냥 email로 비교 할 예정
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (user.getRole() == Role.USER) {
                Token token = tokenService.generateToken(String.valueOf(user.getId()), Role.USER.getKey());
                ResponseCookie cookie = ResponseCookie.from("accessToken", token.getToken())
                        .maxAge(7 * 24 * 60 * 60)
                        .path("/")
                        .httpOnly(true)
                        .build();

                response.setHeader("Set-Cookie", cookie.toString());
                getRedirectStrategy().sendRedirect(request, response, websiteURL);
            } else if (user.getRole() == Role.GUEST) {
                Token token = tokenService.generateToken(String.valueOf(user.getId()), Role.USER.getKey());
                ResponseCookie cookie = ResponseCookie.from("accessToken", token.getToken())
                        .maxAge(7 * 24 * 60 * 60)
                        .path("/")
                        .httpOnly(true)
                        .build();
                response.setHeader("Set-Cookie", cookie.toString());
                getRedirectStrategy().sendRedirect(request, response, websiteURL + "/user/register");
            } else {
                getRedirectStrategy().sendRedirect(request, response, websiteURL + "/user/banned");
            }
        } else {
            User user = userRepository.save(
                    User.builder()
                        .role(Role.GUEST)
                        .nickname("손님")
                        .email(email)
                        .name((String) oAuth2User.getAttribute("name"))
                        .profileImage((String) oAuth2User.getAttribute("picture"))
                        .build()
                    );

            Token token = tokenService.generateToken(String.valueOf(user.getId()), Role.USER.getKey());
            ResponseCookie cookie = ResponseCookie.from("accessToken", token.getToken())
                    .maxAge(7 * 24 * 60 * 60)
                    .path("/")
                    .httpOnly(true)
                    .build();

            response.setHeader("Set-Cookie", cookie.toString());
            getRedirectStrategy().sendRedirect(request, response, websiteURL + "/user/register");
        }
    }
}