package com.rainiersoft.iocl.resources;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Component;

import com.rainiersoft.core.Tag;
import com.rainiersoft.core.TagType;
import com.rainiersoft.ioclparadip.igs.CompactLogixClient;
import com.rainiersoft.ioclparadip.igs.IOCLDButil;
import com.rainiersoft.ioclparadip.igs.IOCLUtil;
import com.rainiersoft.ioclparadip.igs.ResponseFloat;

import javafish.clients.opc.component.OpcItem;

@Path("/truckWt")
@Component
public class TruckWeightManagementResources
{

	public final String PINCODE_VALIDATION_REQUEST= "PinCodeValidation.Req";
	
	public final String bayPrefix ="Bay";
	
	//private int bayNo = 1;
 
	com.rainiersoft.ioclparadip.igs.CompactLogixClient clc = null;
	IOCLDButil ioclUtil = null;

	public TruckWeightManagementResources ()
	{
	}
	
	@Path("/getTruckWeight")
	@GET
	@RolesAllowed({"Admin","Super Admin","TTES Operator","Supervisor"})
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTruckWeight(@QueryParam("bayNum")int bayNum)
	{ 
		float f=0.0f;
		//int bayNum=1;

		ResponseFloat rf= new ResponseFloat();
		
		System.out.println("Initialted DLL loading ....");
		
		clc = CompactLogixClient.getInstance();
		clc.init();
		ioclUtil = new IOCLDButil();
		
		

		System.out.println("BayNumber u are gettingL:"+bayNum);
		// getPinCodeObject filled with Pin Code details.	
		Tag bayNumberTag = getReadTag ( bayNum);
		
		System.out.println("BayNumberTag:"+bayNumberTag);
		System.out.println("*********************before truckWtItem*************************");
		System.out.println("**********************************************");

		OpcItem truckWeightItem = clc.ReadTag(bayNumberTag);
		System.out.println("**********************after truckWeigth item************************");
		System.out.println("**********************************************");
		
		OpcItem items[] = new OpcItem[1];
		items[0] = truckWeightItem;
		clc.populateItems();
		
		System.out.println(truckWeightItem);
		if(null!=truckWeightItem) {
		rf.setTruckWeightValue(truckWeightItem.getValue().getFloat());
		return Response.status(Response.Status.OK).entity(rf).build();
		}

//		clc.deInit();
		//rf.setValue(f);
	return	Response.status(Response.Status.OK).entity(rf).build();
	}
	
	private Tag getReadTag (int bayNumber)
	{
		String tagPrefix ="[TrialTopic]"+bayPrefix+ String.format("%02d", bayNumber)+ "_";
		// read pin code and bay number from plc.
		
		String tagName = tagPrefix + IOCLUtil.TRUCKWEIGHT_WEIGHT;
		TagType tagType = new TagType((String)IOCLUtil.ioclTagMap.get(IOCLUtil.TRUCKWEIGHT_WEIGHT));
		Tag tag = new Tag(tagName, "", tagType, tagName); 
		System.out.println("TagName is::"+tagName);		
		
		return tag;
		
	}
	
	@Path("/getGrossWeight")
	@GET
	@RolesAllowed({"Admin","Super Admin","TTES Operator","Supervisor"})
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getGrossWeight(@QueryParam("bayNum")int bayNum)
	{ 
		float f=0.0f;
		//int bayNum=1;

		ResponseFloat rfgross= new ResponseFloat();
		
		System.out.println("Initialted DLL loading ....");
		
		clc = CompactLogixClient.getInstance();
		clc.init();
		ioclUtil = new IOCLDButil();
		
		

		System.out.println("BayNumber u are gettingL:"+bayNum);
		// getPinCodeObject filled with Pin Code details.	
		Tag bayNumberTag = getReadTag ( bayNum);
		
		System.out.println("BayNumberTag:"+bayNumberTag);
		System.out.println("**********************************************");

		OpcItem truckWeightItem = clc.ReadTag(bayNumberTag);
		System.out.println("**********************************************");
		
		OpcItem items[] = new OpcItem[1];
		items[0] = truckWeightItem;
		clc.populateItems();
		
		System.out.println(truckWeightItem);
		if(null!=truckWeightItem) {
		rfgross.setGrossWeightValue(truckWeightItem.getValue().getFloat());
		return Response.status(Response.Status.OK).entity(rfgross).build();
		}

//		clc.deInit();
		//rf.setValue(f);
	return	Response.status(Response.Status.OK).entity(rfgross).build();
	}
	

	/*public static void main(String[] args) {
		int bayNum = 1;
		TruckWeightManagementResources truckWt=new TruckWeightManagementResources();
		float weight=truckWt.getTruckWeight();
		
		//System.out.println("Truck Weight for bay "+bayNum+"is"+weight);
		
	}
*/}