package com.audictionary.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartRequest;

import com.audictionary.dto.GenFile;
import com.audictionary.dto.ResultData;
import com.audictionary.exceptions.GenFileNotFoundException;
import com.audictionary.service.GenFileService;


@Controller
public class CommonGenFileController {
	@Value("${custom.genFileDirPath}")
	private String genFileDirPath;

	@Autowired
	private GenFileService genFileService;

	@RequestMapping("/common/genFile/doUpload")
	@ResponseBody
	public ResultData doUpload(@RequestParam Map<String, Object> param, MultipartRequest multipartRequest) {
		return genFileService.saveFiles(param, multipartRequest);
	}

	@GetMapping("/common/genFile/doDownload")
	public ResponseEntity<Resource> downloadFile(int id, HttpServletRequest request) throws IOException {
		GenFile genFile = genFileService.getGenFile(id);
		String filePath = genFile.getFilePath(genFileDirPath);

		Resource resource = new InputStreamResource(new FileInputStream(filePath));

		// Try to determine file's content type
		String contentType = request.getServletContext().getMimeType(new File(filePath).getAbsolutePath());

		if (contentType == null) {
			contentType = "application/octet-stream";
		}

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + genFile.getOriginFileName() + "\"")
				.contentType(MediaType.parseMediaType(contentType)).body(resource);
	}

	@GetMapping("/common/genFile/file/{relTypeCode}/{relId}/{typeCode}/{type2Code}/{fileNo}")
	public ResponseEntity<Resource> showFile(HttpServletRequest request, @PathVariable String relTypeCode,
			@PathVariable int relId, @PathVariable String typeCode, @PathVariable String type2Code,
			@PathVariable int fileNo) throws FileNotFoundException {
		GenFile genFile = genFileService.getGenFile(relTypeCode, relId, typeCode, type2Code, fileNo);

		if (genFile == null) {
			throw new GenFileNotFoundException();
		}

		String filePath = genFile.getFilePath(genFileDirPath);
		Resource resource = new InputStreamResource(new FileInputStream(filePath));

		// Try to determine file's content type
		String contentType = request.getServletContext().getMimeType(new File(filePath).getAbsolutePath());

		if (contentType == null) {
			contentType = "application/octet-stream";
		}
		
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(resource);
	}
	
	@GetMapping("/common/genFile/getThumbImgUrl")
	@ResponseBody
	public ResultData getThumbImgUrl( int id ) {
		GenFile genFile = genFileService.getGenFile("ap", id , "common", "attachment", 1);
		
		if ( genFile == null ) {
			return new ResultData ("F-1", "파일이 존재하지 않습니다.");
		}
		
		String imgUrl = genFile.getForPrintUrl();
		
		return new ResultData("S-1", "불러오기 성공", "imgUrl", imgUrl);
	}
	
	@GetMapping("/common/genFile/deleteGenFile")
	@ResponseBody
	public ResultData deleteGenFile( int id ) {
		GenFile genFile = genFileService.getGenFile("ap", id , "common", "attachment", 1);
		
		if ( genFile == null ) {
			return new ResultData ("F-1", "파일이 존재하지 않습니다.");
		}
		
		genFileService.deleteGenFiles("ap", id);
		
		return new ResultData("S-1", "파일이 삭제되었습니다.");
	}
	
}
