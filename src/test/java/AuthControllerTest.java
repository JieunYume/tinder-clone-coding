import com.fasterxml.jackson.databind.ObjectMapper;
import com.hot6.pnureminder.JwtPracticeApplication;
import com.hot6.pnureminder.dto.Member.MemberFindIdReqDto;
import com.hot6.pnureminder.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.BDDMockito.given;
import static org.testng.AssertJUnit.assertTrue;

@AutoConfigureMockMvc
@SpringBootTest(classes = JwtPracticeApplication.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberService memberService;


    // 만약 같은 닉네임과 같은 초등학교를 나온 사람이 있다면?? -> 중복닉네임이 안되게 설정
    @Test
    @WithMockUser
    public void getMemberIdForFindingId() throws Exception {
        MemberFindIdReqDto reqDto = new MemberFindIdReqDto("닉네임", 1, "답변");
        String username = "test1@email.com";

        given(memberService.findUsernameForFindingId(reqDto.getNickname(), reqDto.getFindQuesNum(), reqDto.getFindAnswer()))
                .willReturn(username);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .post("/auth/findingId")
                .content(objectMapper.writeValueAsString(reqDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.content().string(username));
    }

    @Test
    public void getMemberIdForFindingId_notFound() throws Exception {
        MemberFindIdReqDto reqDto = new MemberFindIdReqDto("wrongName", 1, "test1234!!");

        given(memberService.findUsernameForFindingId(reqDto.getNickname(), reqDto.getFindQuesNum(), reqDto.getFindAnswer()))
                .willThrow(new UsernameNotFoundException("User not found with given parameters"));

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .post("/auth/findingId")
                .content(objectMapper.writeValueAsString(reqDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isUnauthorized());


    }

}
