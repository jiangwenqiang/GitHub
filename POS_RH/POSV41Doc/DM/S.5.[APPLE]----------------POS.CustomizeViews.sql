------------------------------------------------------------------------------------------------------------------
-- $Author:: Shihuajun           $  
-- $Date:: 04-02-24 16:28        $  
-- $Modtime:: 04-02-20 15:45     $  
-- $Revision:: 2                 $  
------------------------------------------------------------------------------------------------------------------
--������ͼ
--ע��Ҫʵ���޸�AppleSHStock

SET QUOTED_IDENTIFIER OFF 
GO
SET ANSI_NULLS ON
GO

SET ANSI_NULL_DFLT_ON ON
GO

create view pd(sheetID)
as
   select sheetid from MyShopSHStock..pd0 where flag=2 or flag=1

go